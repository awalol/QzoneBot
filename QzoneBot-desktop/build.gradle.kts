plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":QzoneBot-core"))
    implementation("org.seleniumhq.selenium","selenium-java","4.0.0-beta-3")
}