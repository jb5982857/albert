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

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.tasks.ProcessApplicationManifest
import com.android.build.gradle.tasks.ProcessLibraryManifest
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
        delLaunchActivityForModule(project, isModule)
    }

    private fun configProject(project: Project, isModule: Boolean) {
        if (isModule) {
            project.pluginManager.apply("applicationConfig")
            project.pluginManager.apply("aapplicationAppModule")
        } else {
            project.pluginManager.apply("libraryConfig")
        }
        project.pluginManager.apply("org.jetbrains.kotlin.android")
        project.pluginManager.apply("arouter")
        project.pluginManager.apply("resourcePrefix")
    }

    private fun getAllVariantManifestTask(
        project: Project, isModule: Boolean
    ) {
        if (!isModule) {
            project.extensions.findByType(BaseExtension::class.java)?.variantFilter {
                variantNames.add(this.name)
            }
        } else {
            project.extensions.findByType(AppExtension::class.java)?.variantFilter {
                variantNames.add(this.name)
            }
        }
    }

    private fun delLaunchActivityForModule(project: Project, isModule: Boolean) {

        project.afterEvaluate {
            variantNames.forEach {
                if (isModule) {
                    val manifestTask: ProcessApplicationManifest = project.tasks.getByName(
                        String.format(
                            "process%sMainManifest", it.capitalize()
                        )
                    ) as ProcessApplicationManifest
                    manifestTask.doLast {
                        val task = this as ProcessApplicationManifest
                        val androidManifestPath = task.mergedManifest.get().toString()
                        ManifestUtils.addApplicationIfNeeded(androidManifestPath)
                    }
                } else {
                    val manifestTask: ProcessLibraryManifest = project.tasks.getByName(
                        String.format(
                            "process%sManifest", it.capitalize()
                        )
                    ) as ProcessLibraryManifest
                    manifestTask.doLast {
                        val task = this as ProcessLibraryManifest
                        val androidManifestPath = task.manifestOutputFile.get().asFile.absolutePath
                        ManifestUtils.delLaunchActivityIfNeeded(androidManifestPath)
                    }
                }
            }
        }
    }
}