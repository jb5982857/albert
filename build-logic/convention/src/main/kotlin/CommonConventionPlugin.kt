import org.gradle.api.Plugin
import org.gradle.api.Project

class CommonConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("com.android.library")
        project.pluginManager.apply("org.jetbrains.kotlin.android")
        project.pluginManager.apply("arouter")
        project.pluginManager.apply("resourcePrefix")
    }
}