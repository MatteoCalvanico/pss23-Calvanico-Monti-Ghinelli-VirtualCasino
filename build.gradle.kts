plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building a CLI application
    // You can run your app via task "run": ./gradlew run
    application

    /*
     * Adds tasks to export a runnable jar.
     * In order to create it, launch the "shadowJar" task.
     * The runnable jar will be found in build/libs/projectname-all.jar
     */
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

val javaFXModules = listOf(
    "base",
    "controls",
    "fxml",
    "media", // Required for sound
    "swing",
    "graphics"
)

val supportedPlatforms = listOf("linux", "mac", "win") // All required for OOP
val testFxVersion = "4.0.16-alpha"

dependencies {
    // Suppressions for SpotBugs
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.8.3")

    // Library
    implementation("org.openjfx:javafx-controls:17.0.1") // JavaFX Controls
    implementation("org.openjfx:javafx-media:17.0.1")    // JavaFX Media (sound)
    implementation("com.google.code.gson:gson:2.8.8")    // JSON serialization and deserialization

    // JavaFX: comment out if you do not need them
    val javaFxVersion = "17.0.1"
    for (platform in supportedPlatforms) {
        for (module in javaFXModules) {
            implementation("org.openjfx:javafx-$module:$javaFxVersion:$platform")
        }
    }

    val jUnitVersion = "5.10.1"
    // JUnit API and testing engine
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jUnitVersion")
    testImplementation("org.testfx:testfx-junit5:$testFxVersion")
    testRuntimeOnly("org.testfx:openjfx-monocle:17.0.1")
}

tasks.withType<Test> {
    // Enables JUnit 5 Jupiter module
    useJUnitPlatform()
    
    //proprietà usate da TestFX/Monocle per la modalità head-less
    systemProperty("testfx.headless", "true")
    systemProperty("testfx.robot",    "glass")
    systemProperty("prism.order",     "sw")
    systemProperty("java.awt.headless", "true")
}

application {
    // Define the main class for the application
    mainClass.set("it.unibo.virtualCasino.App")
}
