/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.AppExtension
import com.android.build.gradle.tasks.ProcessApplicationManifest
import org.gradle.api.Plugin
import org.gradle.api.Project
import utils.ManifestUtils

class ModuleConventionPlugin : Plugin<Project> {
    private val variantNames = mutableListOf<String>()
    override fun apply(project: Project) {
        val isModule = System.getProperty("isModule").toString().toBoolean()
        println("isModule: $isModule")

        configProject(project, isModule)
        getAllVariantManifestTask(project, isModule)
        addLaunchActivityForModule(project, isModule)
    }

    private fun configProject(project: Project, isModule: Boolean) {
        if (isModule) {
            project.pluginManager.apply("com.android.application")
            project.pluginManager.apply("aapplicationAppModule")
        } else {
            project.pluginManager.apply("com.android.library")
        }
        project.pluginManager.apply("org.jetbrains.kotlin.android")
        project.pluginManager.apply("arouter")
        project.pluginManager.apply("resourcePrefix")

        val android = project.extensions.getByName("android")
        if (android is ApplicationExtension) {
            android.defaultConfig.let { defaultConfig ->
                defaultConfig.applicationId = "com.albert.${project.name}"
                defaultConfig.versionCode = 1
                defaultConfig.versionName = "1.0.0"
            }
        }
    }

    private fun getAllVariantManifestTask(
        project: Project, isModule: Boolean
    ) {
        if (isModule) {
            project.extensions.findByType(AppExtension::class.java)?.variantFilter {
                variantNames.add(this.name)
            }
        }
    }

    private fun addLaunchActivityForModule(project: Project, isModule: Boolean) {
        if (!isModule) {
            return
        }

        project.afterEvaluate {
            variantNames.forEach {
                val applicationManifestTask: ProcessApplicationManifest = project.tasks.getByName(
                    String.format(
                        "process%sMainManifest", it.capitalize()
                    )
                ) as ProcessApplicationManifest
                applicationManifestTask.doLast {
                    val task = this as ProcessApplicationManifest
                    val androidManifestPath = task.mergedManifest.get().toString()
                    ManifestUtils.addLaunchActivityIfNeeded(androidManifestPath)
                }
            }
        }
    }
}