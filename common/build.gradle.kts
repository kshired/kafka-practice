tasks.getByName("jar") {
    enabled = true
}

dependencies {
    api("org.apache.kafka:kafka-clients:${property("kafkaClientsVersion")}")
    api("org.slf4j:slf4j-simple:${property("slf4jVersion")}")
}
