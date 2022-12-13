package appconfig

import org.gradle.api.Project
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.File

class AppConfigBean {
    var storeFilePath = ""
    var keyPass = ""
    var storePass = ""
    var alias = ""

    var applicationId = ""
    var versionCode = 0
    var versionName = ""
    var target = 0
    var min = 0
    var compileSdk = 0

    fun assertMessageValid(isApplication: Boolean) {
        if (storeFilePath.isEmpty()) {
            throw RuntimeException("storeFilePath is Empty,please check it")
        }
        if (keyPass.isEmpty()) {
            throw RuntimeException("keyPass is Empty,please check it")
        }
        if (storePass.isEmpty()) {
            throw RuntimeException("storePass is Empty,please check it")
        }
        if (alias.isEmpty()) {
            throw RuntimeException("alias is Empty,please check it")
        }
        if (applicationId.isEmpty()) {
            if (isApplication) {
                throw RuntimeException("applicationId is Empty,please check it")
            }
        }
        if (versionCode == 0) {
            if (isApplication) {
                throw RuntimeException("versionCode is Empty,please check it")
            }
        }
        if (versionName.isEmpty()) {
            if (isApplication) {
                throw RuntimeException("versionName is Empty,please check it")
            }
        }

        if (target == 0) {
            throw RuntimeException("target is Empty,please check it")
        }

        if (min == 0) {
            throw RuntimeException("min is Empty,please check it")
        }

        if (compileSdk == 0) {
            throw RuntimeException("compileSdk is Empty,please check it")
        }
    }

    companion object {
        fun parseProperties(
            project: Project, properties: Properties, isApplication: Boolean
        ): AppConfigBean {
            val result = AppConfigBean()
            result.storeFilePath = properties.getProperty("storeFilePath", "")
            if (result.storeFilePath.isNotEmpty()) {
                result.storeFilePath =
                    File(project.rootProject.file("./").absolutePath + File.separator + result.storeFilePath).absolutePath
            }
            result.keyPass = properties.getProperty("keyPass", "")
            result.storePass = properties.getProperty("storePass", "")
            result.alias = properties.getProperty("alias", "")
            result.applicationId = properties.getProperty("applicationId", "")
            result.versionCode = properties.getProperty("versionCode", "0").toInt()
            result.versionName = properties.getProperty("versionName", "")
            result.target = properties.getProperty("target", "0").toInt()
            result.min = properties.getProperty("min", "0").toInt()
            result.compileSdk = properties.getProperty("compileSdk", "0").toInt()
            result.assertMessageValid(isApplication)
            return result
        }
    }
}