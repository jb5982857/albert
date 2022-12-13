package appconfig

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.File
import java.io.FileInputStream

class LibraryConfigPlugin : Plugin<Project> {
    companion object {
        const val GRADLE_PROPERTIES = "gradle.properties"
    }

    override fun apply(project: Project) {
        val file = project.rootProject.file(GRADLE_PROPERTIES)
        if (!file.exists()) {
            throw RuntimeException("gradle.properties in ${file.absolutePath} is not exist")
        }
        val inputStream = FileInputStream(file)
        val properties = Properties()
        properties.load(inputStream)
        val appConfigBean = AppConfigBean.parseProperties(project, properties, false)

        project.pluginManager.apply("com.android.library")

        val android = project.extensions.getByName("android")
        if (android is BaseExtension) {
            android.setCompileSdkVersion(appConfigBean.compileSdk)
            android.defaultConfig.let { defaultConfig ->
                defaultConfig.targetSdk = appConfigBean.target
                defaultConfig.minSdk = appConfigBean.min
            }
            android.signingConfigs {
                this.create("release") {
                    this.storeFile = File(appConfigBean.storeFilePath)
                    this.enableV2Signing = true
                    this.keyAlias = appConfigBean.alias
                    this.keyPassword = appConfigBean.keyPass
                    this.storePassword = appConfigBean.storePass
                }
            }
        } else {
            throw RuntimeException("plugin libraryConfig is not used to a library, maybe is a application,check it !!!")
        }
    }
}

