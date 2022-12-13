import org.gradle.api.Plugin
import org.gradle.api.Project

class AppConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("applicationConfig")
        project.pluginManager.apply("org.jetbrains.kotlin.android")
        project.pluginManager.apply("aapplicationLifecycle")
        project.pluginManager.apply("aapplicationAppModule")
        project.pluginManager.apply("resourcePrefix")
    }
}