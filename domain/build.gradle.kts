plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.4.7")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation ("io.projectreactor:reactor-test")
    implementation("org.slf4j:slf4j-api:2.0.17")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

