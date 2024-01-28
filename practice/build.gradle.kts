tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":common"))
    implementation("com.github.javafaker:javafaker:${property("javaFakerVersion")}")
    implementation("org.postgresql:postgresql:${property("postgresqlVersion")}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${property("jacksonVersion")}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${property("jacksonVersion")}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${property("jacksonVersion")}")
}
