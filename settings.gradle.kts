pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "file:///Users/jiangbo/Documents/android/albert/build-logic/repo")
    }
}

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "file:///Users/jiangbo/Documents/android/albert/build-logic/repo")
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
include(":application-lifecycle-api")
project(":application-lifecycle-api").projectDir = File("./build-logic/application-lifecycle-api")
