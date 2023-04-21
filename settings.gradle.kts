rootProject.name = "free-storm-plugins"


include(":cannonhelper")
include(":museumcleaner")
include(":winemaker")
include(":woodcutter")
include(":chinbreakhandler")
include(":zerotickcombiner")
include(":prayerflicker")
include(":glassblower")
include(":miner")
include(":gemcutter")
include(":thief")
include(":autologhop")
include(":quickherbcleaner")

for (project in rootProject.children) {
    project.apply {
        projectDir = file(name)
        buildFileName = "$name.gradle.kts"

        require(projectDir.isDirectory) { "Project '${project.path} must have a $projectDir directory" }
        require(buildFile.isFile) { "Project '${project.path} must have a $buildFile build script" }
    }
}