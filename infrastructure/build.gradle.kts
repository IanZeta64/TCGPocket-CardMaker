plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive:3.4.7")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
