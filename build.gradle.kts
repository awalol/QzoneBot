plugins {
    kotlin("jvm") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

allprojects{
    group = "cn.awalol"
    version = "2.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

/*tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "cn.awalol.qzoneBot.MainKt"
    }
}*/
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>{
    kotlinOptions.jvmTarget = "1.8"
}
