plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

dependencies {
    api(project(":QzoneBot-core"))
    implementation("org.seleniumhq.selenium","selenium-java","4.0.0-beta-3")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    this.destinationDir = file("$rootDir/build/libs")
    /*manifest {
        attributes["Main-Class"] = "cn.awalol.qzoneBot.MainKt"
    }*/
}