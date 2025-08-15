plugins {
    `java-library`
    `maven-publish`
}

group = "io.printkit"
version = "0.1.0-SNAPSHOT"

dependencies {
    api("org.processing:core:4.3.1")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
