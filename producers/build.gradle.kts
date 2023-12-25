tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation("com.github.javafaker:javafaker:${property("javaFakerVersion")}")
}
