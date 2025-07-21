//plugins {
//	id("org.springframework.boot") version "3.4.7"
//	id("io.spring.dependency-management") version "1.1.7"
//	java
//}
//
//springBoot {
//	mainClass.set("br.com.tcgpocket.cardmaker.CardMakerApplication")
//}
//
//group = "br.com.tcgpocket"
//version = "0.0.1-SNAPSHOT"
//
//java {
//	toolchain {
//		languageVersion = JavaLanguageVersion.of(21)
//	}
//}
//
//repositories {
//	mavenCentral()
//}
//
//extra["springCloudVersion"] = "2024.0.1"
//
//dependencyManagement {
//	imports {
//		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
//	}
//}
//
//tasks.withType<Test> {
//	useJUnitPlatform()
//}
//
//tasks.register<JavaExec>("bootRunApp") {
//	group = "application"
//	description = "Run bootRun on app module"
//
//	classpath = project(":app").sourceSets["main"].runtimeClasspath
//	mainClass.set("br.com.tcgpocket.cardmaker.CardMakerApplication")
//}


///////////////////////////////////

plugins {
	id("org.springframework.boot") version "3.4.7"
	id("io.spring.dependency-management") version "1.1.7"
	java
}

group = "br.com.tcgpocket"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

repositories {
	mavenCentral()
}


tasks.withType<Test> {
	useJUnitPlatform()
}

// Tarefa bootRun para rodar o módulo 'app'
tasks.register<JavaExec>("bootRunApp") {
	group = "application"
	description = "Run Spring Boot app module"

	classpath = project(":app").sourceSets["main"].runtimeClasspath
	mainClass.set("br.com.tcgpocket.cardmaker.CardMakerApplication")

	// Se quiser passar argumentos para o app, use args(...)
}