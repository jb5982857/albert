import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class AApplicationLifecycleConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("kotlin-kapt")
        setDependencies(project)
    }

    private fun setDependencies(project: Project) {
        project.dependencies {
            this.add("implementation", "com.albert:application-lifecycle-annotation:0.1.0")
            this.add("kapt", "com.albert:application-lifecycle-compiler:0.1.0")
            this.add("implementation", project(":application-lifecycle-api"))
        }
    }
}