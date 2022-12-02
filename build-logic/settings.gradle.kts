enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("depend") {
            from(files("../gradle/depend.versions.toml"))
        }
    }
}

include(":convention")