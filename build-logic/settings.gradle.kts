pluginManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }

    versionCatalogs {
        create("depend") {
            from(files("../gradle/depend.versions.toml"))
        }
    }
}

include(":convention")
include(":application-lifecycle-compiler")
include(":application-lifecycle-annotation")
//include(":application-lifecycle-api")