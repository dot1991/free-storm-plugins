plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    `java-library`
}

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

project.extra["GithubUrl"] = "https://github.com/blogans/unethicalite-plugins"

apply<BootstrapPlugin>()

subprojects {
    group = "net.unethicalite.externals"

    project.extra["PluginProvider"] = "Hori"
    project.extra["ProjectSupportUrl"] = ""
    project.extra["PluginLicense"] = "3-Clause BSD License"

    apply<JavaPlugin>()
    apply(plugin = "java-library")
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        mavenLocal()

        maven {
            url = uri("https://repo.unethicalite.net/releases/")
            mavenContent {
                releasesOnly()
            }
        }
        maven {
            url = uri("https://repo.unethicalite.net/snapshots/")
            mavenContent {
                snapshotsOnly()
            }
        }
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        annotationProcessor(Libraries.lombok)

        compileOnly("net.unethicalite:runelite-api:${ProjectVersions.unethicaliteVersion}+")
        compileOnly("net.unethicalite:runelite-client:${ProjectVersions.unethicaliteVersion}+")
        compileOnly("net.unethicalite.rs:runescape-api:${ProjectVersions.unethicaliteVersion}+")
        compileOnly("net.unethicalite:http-api:${ProjectVersions.unethicaliteVersion}+")

        compileOnly(Libraries.guice)
        compileOnly(Libraries.javax)
        compileOnly(Libraries.lombok)
        compileOnly(Libraries.pf4j)
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks {
        compileKotlin {
            kotlinOptions {
                jvmTarget = "11"
                freeCompilerArgs = listOf("-Xjvm-default=enable")
            }
            sourceCompatibility = "11"
        }

        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        withType<Jar> {
            doLast {
                copy {
                    from("./build/libs/")
                    into("${System.getProperty("user.home")}/.openosrs/plugins")
                }
            }
        }

        withType<AbstractArchiveTask> {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
            dirMode = 493
            fileMode = 420
        }

        withType<Checkstyle> {
            group = "verification"
        }

        register<Copy>("copyDeps") {
            into("./build/deps/")
            from(configurations["runtimeClasspath"])
        }

    }
}
