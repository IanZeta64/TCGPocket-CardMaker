plugins {
    id("org.springframework.boot") version "3.4.7"
    id("io.spring.dependency-management") version "1.1.7"
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":infrastructure"))
    implementation(project(":domain"))
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.7")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.8.10")
    //    implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.withType<Test> {
    useJUnitPlatform()
}