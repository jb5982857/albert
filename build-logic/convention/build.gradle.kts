plugins {
    `kotlin-dsl`
}

plugins.apply("groovy")

group = "com.albert.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    //gradle sdk
    implementation(gradleApi())
    //groovy sdk
    implementation(localGroovy())
    implementation(depend.android.gradlePlugin)
    implementation(depend.kotlin.gradlePlugin)
    implementation(depend.spotless.gradlePlugin)
    implementation("commons-io:commons-io:2.4")
    implementation("commons-codec:commons-codec:1.11")
    implementation(files("libs/dom4j-2.0.3.jar"))
}

gradlePlugin {
    plugins {
        register("module") {
            id = "module"
            implementationClass = "ModuleConventionPlugin"
        }

        register("arouter") {
            id = "arouter"
            implementationClass = "ARouterConventionPlugin"
        }

        register("resourcePrefix") {
            id = "resourcePrefix"
            implementationClass = "ResourcePrefixConventionPlugin"
        }

        register("common") {
            id = "common"
            implementationClass = "CommonConventionPlugin"
        }

        register("library") {
            id = "library"
            implementationClass = "LibraryConventionPlugin"
        }
        register("app") {
            id = "app"
            implementationClass = "AppConventionPlugin"
        }

        register("aapplicationAppModule") {
            id = "aapplicationAppModule"
            implementationClass = "AApplicationConventionTransform"
        }

        register("aapplicationLifecycle") {
            id = "aapplicationLifecycle"
            implementationClass = "AApplicationLifecycleConventionPlugin"
        }

        register("applicationConfig") {
            id = "applicationConfig"
            implementationClass = "appconfig.ApplicationConfigPlugin"
        }

        register("libraryConfig") {
            id = "libraryConfig"
            implementationClass = "appconfig.LibraryConfigPlugin"
        }

    }
}
