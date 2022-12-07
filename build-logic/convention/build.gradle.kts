plugins {
    `kotlin-dsl`
}

group = "com.albert.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(depend.android.gradlePlugin)
    implementation(depend.kotlin.gradlePlugin)
    implementation(depend.spotless.gradlePlugin)
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
    }
}
