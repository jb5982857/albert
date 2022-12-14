
import asm.AApplicationWriter
import com.android.build.api.transform.*
import com.android.build.api.transform.QualifiedContent.ContentType
import com.android.build.api.transform.QualifiedContent.DefaultContentType
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import utils.eachFileRecurse
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class AApplicationConventionTransform : Transform(), Plugin<Project> {
    private val aptClassPackages = mutableListOf<String>()

    companion object {
        const val ALBERT_CLASS_PACKAGE = "com/albert/application/lifecycle/apt"
        const val NEED_DEL_CLASS = "com/albert/application/lifecycle/apt/AApplicationApts.class"
    }

    override fun apply(project: Project) {
        val android = project.extensions.getByType(AppExtension::class.java)
        android.registerTransform(this)
    }

    override fun getName(): String {
        return "aapplication"
    }

    override fun getInputTypes(): MutableSet<ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun transform(transformInvocation: TransformInvocation) {
        val inputs = transformInvocation.inputs
        val outputProvider = transformInvocation.outputProvider
        outputProvider?.deleteAll()
        inputs.forEach { input ->
            input.directoryInputs.forEach { directoryInput ->
                handleDirectoryInput(directoryInput, outputProvider)
            }

            val startTime = System.currentTimeMillis()
            input.jarInputs.forEach { jarInput ->
                handleJarInputs(jarInput, outputProvider)
            }
            println("?????? jar ???????????????${System.currentTimeMillis() - startTime}")
        }
        writerAApplicationToNewClass(outputProvider)
    }

    private fun writerAApplicationToNewClass(outputProvider: TransformOutputProvider) {
        val dest = outputProvider.getContentLocation(
            "aapplication",
            mutableSetOf(DefaultContentType.CLASSES) as Set<ContentType>?,
            mutableSetOf(QualifiedContent.Scope.PROJECT),
            Format.DIRECTORY
        ).absolutePath + File.separator + AApplicationWriter.CLASS_NAME
        println("aapplication ?????????????????????$dest")
        AApplicationWriter(aptClassPackages).start(dest)
    }

    private fun handleDirectoryInput(
        directoryInput: DirectoryInput, outputProvider: TransformOutputProvider
    ) {
        if (directoryInput.file.isDirectory) {
            directoryInput.file.eachFileRecurse { file ->
                val path = file.absolutePath
                if (checkClassFile(path)) {
                    val name = ALBERT_CLASS_PACKAGE + "/" + file.name
                    println("----------- deal with class file < $name > -----------")
                    aptClassPackages.add(classPathTransformJavaClass(name))
                }
            }
        }
        //????????????????????????????????????????????????????????????
        val dest = outputProvider.getContentLocation(
            directoryInput.name,
            directoryInput.contentTypes,
            directoryInput.scopes,
            Format.DIRECTORY
        )
        println("copy: from ${directoryInput.file} to $dest???${directoryInput.contentTypes},${directoryInput.scopes}")
        FileUtils.copyDirectory(directoryInput.file, dest)
    }

    private fun classPathTransformJavaClass(classPath: String): String {
        return classPath.substring(0, classPath.length - 6)
    }

    private fun handleJarInputs(jarInput: JarInput, outputProvider: TransformOutputProvider) {
        if (jarInput.file.absolutePath.endsWith(".jar")) {
            val startTime = System.currentTimeMillis()
            //?????????????????????,??????????????????,?????????
            var jarName = jarInput.name
            val md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length - 4)
            }
            val jarFile = JarFile(jarInput.file)
            val enumeration = jarFile.entries()
            val tmpFile = File(jarInput.file.parent + File.separator + "classes_temp.jar")
            //????????????????????????????????????
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            val jarOutputStream = JarOutputStream(FileOutputStream(tmpFile))
            //????????????
            while (enumeration.hasMoreElements()) {
                val jarEntry = enumeration.nextElement() as JarEntry
                val entryName = jarEntry.name
                val zipEntry = ZipEntry(entryName)
                val inputStream = jarFile.getInputStream(jarEntry)
                //??????class
                var needDel = false
                if (checkClassFile(entryName)) {
                    if (needDel(entryName)) {
                        println("?????????$NEED_DEL_CLASS?????? $entryName")
                        needDel = true
                    } else {
                        //class????????????
                        println("----------- deal with jar class file < $entryName > -----------")
                        aptClassPackages.add(classPathTransformJavaClass(entryName))
                    }
                }

                if (!needDel) {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                    jarOutputStream.closeEntry()
                }
            }
            //??????
            jarOutputStream.close()
            jarFile.close()
            val dest = outputProvider.getContentLocation(
                jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR
            )
            FileUtils.copyFile(tmpFile, dest)
            tmpFile.delete()
            println("?????? jar $jarName ??????????????? ${System.currentTimeMillis() - startTime}")
        }
    }


    private fun needDel(name: String) =
        (name.endsWith(".class") && !name.startsWith("R\$") && "R.class" != name && "BuildConfig.class" != name && name.contains(
            NEED_DEL_CLASS
        ))

    /**
     * ??????class????????????????????????
     * @param fileName
     * @return
     */
    private fun checkClassFile(name: String): Boolean {
        //??????????????????class??????
        return (name.endsWith(".class") && !name.startsWith("R\$") && "R.class" != name && "BuildConfig.class" != name && name.contains(
            ALBERT_CLASS_PACKAGE
        ))
    }
}