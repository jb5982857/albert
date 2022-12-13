package appconfig

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.File
import java.io.FileInputStream

class ApplicationConfigPlugin : Plugin<Project> {
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
        val appConfigBean = AppConfigBean.parseProperties(project, properties, true)

        project.pluginManager.apply("com.android.application")

        val android = project.extensions.getByName("android")
        if (android is ApplicationExtension) {
            android.compileSdk = appConfigBean.compileSdk
            android.defaultConfig.let { defaultConfig ->
                defaultConfig.applicationId = appConfigBean.applicationId
                defaultConfig.versionCode = appConfigBean.versionCode
                defaultConfig.versionName = appConfigBean.versionName
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
            throw RuntimeException("plugin applicationConfig is not used to a application, maybe is a library,check it !!!")
        }
    }
}

