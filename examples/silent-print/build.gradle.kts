plugins { application }

application {
    mainClass.set("demo.SilentPrintSketch")
}

dependencies {
    implementation(project(":library"))
    implementation("org.processing:core:4.3.1")
}
