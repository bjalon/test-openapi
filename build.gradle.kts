allprojects {
    group = project.findProperty("group") as String
    version = project.findProperty("version") as String

    repositories {
        mavenCentral()
    }


}