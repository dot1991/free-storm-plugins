version = "0.0.2"
project.extra["PluginName"] = "Quick Herb Cleaner" // This is the name that is used in the external plugin manager panel
project.extra["PluginDescription"] = "An automatic herb cleaning plugin" // This is the description that is used in the external plugin manager panel

plugins{
    kotlin("kapt")
}

dependencies {
    kapt(Libraries.pf4j)
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = listOf("-Xjvm-default=enable")
        }
    }
    jar {
        manifest {
            attributes(mapOf(
                "Plugin-Version" to project.version,
                "Plugin-Id" to nameToId(project.extra["PluginName"] as String),
                "Plugin-Provider" to project.extra["PluginProvider"],
                "Plugin-Description" to project.extra["PluginDescription"],
                "Plugin-License" to project.extra["PluginLicense"]
            ))
        }
    }
}