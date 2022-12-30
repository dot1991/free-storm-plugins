import ProjectVersions.stormVersion

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    `java-library`
    kotlin("jvm") version "1.6.21"
}

project.extra["GithubUrl"] = "https://github.com/blogans"
project.extra["GithubUserName"] = "Blogans"
project.extra["GithubRepoName"] = "free-storm-plugins"

apply<BootstrapPlugin>()

allprojects {
    group = "net.unethicalite"

    project.extra["PluginProvider"] = "subaru"
    project.extra["ProjectSupportUrl"] = "https://discord.gg/"
    project.extra["PluginLicense"] = "3-Clause BSD License"

    apply<JavaPlugin>()
    apply(plugin = "java-library")
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://repo.storm-client.net/dev/")
            credentials {
                username = System.getenv("REPO_USERNAME")
                username = System.getenv("REPO_PASSWORD")
            }
        }
    }

    dependencies {
        annotationProcessor(Libraries.lombok)
        annotationProcessor(Libraries.pf4j)

        annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.20")
        annotationProcessor(group = "org.pf4j", name = "pf4j", version = "3.6.0")

        compileOnly("net.storm:http-api:${ProjectVersions.stormVersion}")
        compileOnly("net.storm:runelite-api:${ProjectVersions.stormVersion}")
        compileOnly("net.storm:runelite-client:${ProjectVersions.stormVersion}")

        compileOnly(group = "com.google.code.gson", name = "gson", version = "2.8.5")

        implementation("org.jetbrains.kotlin:kotlin-stdlib")

        compileOnly(group = "com.squareup.okhttp3", name = "okhttp", version = "3.7.0")
        compileOnly(group = "org.apache.commons", name = "commons-text", version = "1.2")
        compileOnly(group = "com.google.code.gson", name = "gson", version = "2.8.5")
        compileOnly(group = "com.google.inject", name = "guice", version = "5.0.1")
        compileOnly(group = "io.reactivex.rxjava3", name = "rxjava", version = "3.1.1")
        compileOnly(group = "org.apache.commons", name = "commons-text", version = "1.9")
        compileOnly(group = "com.google.guava", name = "guava", version = "30.1.1-jre") {
            exclude(group = "com.google.code.findbugs", module = "jsr305")
            exclude(group = "com.google.errorprone", module = "error_prone_annotations")
            exclude(group = "com.google.j2objc", module = "j2objc-annotations")
            exclude(group = "org.codehaus.mojo", module = "animal-sniffer-annotations")
        }
        compileOnly(group = "com.google.inject", name = "guice", version = "5.0.1")
        compileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.4")
        compileOnly(group = "org.pf4j", name = "pf4j", version = "3.6.0")
        compileOnly(group = "io.reactivex.rxjava3", name = "rxjava", version = "3.1.1")
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
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        withType<AbstractArchiveTask> {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
            dirMode = 493
            fileMode = 420
        }

        compileKotlin {
            kotlinOptions.jvmTarget = "11"
        }
    }
}