plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.data:spring-data-mongodb:4.2.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

