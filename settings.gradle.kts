pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("depend") {
            from(files("./gradle/depend.versions.toml"))
        }
    }
}
rootProject.name = "Albert"
include(":app")
include(":common")


include(":account")
include(":app-utils")
include(":common-ui")
include(":network")
include(":data-store")
include(":log")
