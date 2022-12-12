import asm.AApplicationWriter
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.api.transform.*
import com.android.build.api.transform.QualifiedContent.ContentType
import com.android.build.api.transform.QualifiedContent.DefaultContentType
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import utils.eachFileRecurse
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.apache.commons.codec.digest.DigestUtils
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

            input.jarInputs.forEach { jarInput ->
                handleJarInputs(jarInput, outputProvider)
            }
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
        println("aapplication 写入的路径为：$dest")
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
        //处理完输入文件之后，要把输出给下一个任务
        val dest = outputProvider.getContentLocation(
            directoryInput.name,
            directoryInput.contentTypes,
            directoryInput.scopes,
            Format.DIRECTORY
        )
        println("copy: from ${directoryInput.file} to $dest，${directoryInput.contentTypes},${directoryInput.scopes}")
        FileUtils.copyDirectory(directoryInput.file, dest)
    }

    private fun classPathTransformJavaClass(classPath: String): String {
        return classPath.substring(0, classPath.length - 6)
    }

    private fun handleJarInputs(jarInput: JarInput, outputProvider: TransformOutputProvider) {
        if (jarInput.file.absolutePath.endsWith(".jar")) {
            val startTime = System.currentTimeMillis()
            //重名名输出文件,因为可能同名,会覆盖
            var jarName = jarInput.name
            val md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length - 4)
            }
            val jarFile = JarFile(jarInput.file)
            val enumeration = jarFile.entries()
            val tmpFile = File(jarInput.file.parent + File.separator + "classes_temp.jar")
            //避免上次的缓存被重复插入
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            val jarOutputStream = JarOutputStream(FileOutputStream(tmpFile))
            //用于保存
            while (enumeration.hasMoreElements()) {
                val jarEntry = enumeration.nextElement() as JarEntry
                val entryName = jarEntry.name
                val zipEntry = ZipEntry(entryName)
                val inputStream = jarFile.getInputStream(jarEntry)
                //插桩class
                if (checkClassFile(entryName)) {
                    //class文件处理
                    println("----------- deal with jar class file < $entryName > -----------")
                    aptClassPackages.add(classPathTransformJavaClass(entryName))
                }
                jarOutputStream.putNextEntry(zipEntry)
                jarOutputStream.write(IOUtils.toByteArray(inputStream))
                jarOutputStream.closeEntry()
            }
            //结束
            jarOutputStream.close()
            jarFile.close()
            val dest = outputProvider.getContentLocation(
                jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR
            )
            FileUtils.copyFile(tmpFile, dest)
            tmpFile.delete()
            println("解析 jar $jarName ，总共费时 ${System.currentTimeMillis() - startTime}")
        }
    }


    /**
     * 检查class文件是否需要处理
     * @param fileName
     * @return
     */
    private fun checkClassFile(name: String): Boolean {
        //只处理需要的class文件
        return (name.endsWith(".class") && !name.startsWith("R\$") && "R.class" != name && "BuildConfig.class" != name && name.contains(
            ALBERT_CLASS_PACKAGE
        ))
    }
}