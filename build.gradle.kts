plugins {
	java
	id("org.springframework.boot") version "3.5.7"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.synko"
version = "0.0.1-SNAPSHOT"
description = "Sistema de gestao finanaceira"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("com.h2database:h2")
    implementation("com.google.firebase:firebase-admin:9.2.0")
    implementation(platform("com.google.guava:guava-bom:33.2.1-jre"))
    implementation(platform("com.google.protobuf:protobuf-bom:3.25.5"))
    implementation("org.springframework.boot:spring-boot-starter-security")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
