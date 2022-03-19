plugins {
    id ("org.jetbrains.kotlin.jvm") version ("1.6.10")
    id ("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    implementation(fileTree(baseDir = "gradle/wrapper"){
        include("**/*.jar")
    })
}

tasks.withType<Test>{
    useJUnitPlatform()
}