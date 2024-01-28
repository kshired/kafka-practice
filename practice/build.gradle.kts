tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":common"))
    implementation("com.github.javafaker:javafaker:${property("javaFakerVersion")}")
}
