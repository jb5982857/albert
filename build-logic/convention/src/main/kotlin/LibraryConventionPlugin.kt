import org.gradle.api.Plugin
import org.gradle.api.Project

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("libraryConfig")
        project.pluginManager.apply("org.jetbrains.kotlin.android")
        project.pluginManager.apply("resourcePrefix")
    }
}