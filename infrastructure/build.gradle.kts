plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive:3.4.7")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
