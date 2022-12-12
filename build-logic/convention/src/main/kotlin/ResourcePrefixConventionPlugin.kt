import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class ResourcePrefixConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val android = project.extensions.getByName("android")
        (android as CommonExtension<*, *, *, *>).resourcePrefix =
            project.name.replace("-", "_") + "_"
    }
}