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
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.4.7")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.7")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.7")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation ("io.projectreactor:reactor-test")
    implementation("org.slf4j:slf4j-api:2.0.17")
    //    implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.withType<Test> {
    useJUnitPlatform()
}