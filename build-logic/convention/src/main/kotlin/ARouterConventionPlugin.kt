import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class ARouterConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("kotlin-kapt")
        setKapt(project)
        setDependencies(project)
    }


    private fun setKapt(project: Project) {
        project.extensions.configure<KaptExtension>("kapt") {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
    }

    private fun setDependencies(project: Project) {
        project.dependencies {
            this.add("implementation", "com.alibaba:arouter-api:1.5.2")
            this.add("kapt", "com.alibaba:arouter-compiler:1.5.2")
        }
    }
}