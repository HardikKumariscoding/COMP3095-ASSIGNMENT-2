plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "ca.gbc"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
}

extra["snippetsDir"] = file("build/generated-snippets")
//extra["springCloudVersion"] = "2024.0.0-RC1"
extra["springCloudVersion"] = "2023.0.3"


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
//	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
//	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.cloud:spring-cloud-starter-gateway")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
	implementation ("io.micrometer:micrometer-registry-prometheus")
//	implementation("org.flywaydb:flyway-core")
//	implementation("org.flywaydb:flyway-database-postgresql")
//	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
//	compileOnly("org.projectlombok:lombok")
//	developmentOnly("org.springframework.boot:spring-boot-devtools")
//	runtimeOnly("org.postgresql:postgresql")
//	annotationProcessor("org.projectlombok:lombok")
//	testImplementation("org.springframework.boot:spring-boot-starter-test")
//	testImplementation("org.springframework.boot:spring-boot-testcontainers")
//	testImplementation("io.projectreactor:reactor-test")
//	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
//	testImplementation("org.testcontainers:junit-jupiter")
//	testImplementation("org.testcontainers:mongodb")
//	testImplementation("org.testcontainers:postgresql")
//	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	outputs.dir(project.extra["snippetsDir"]!!)
}

tasks.asciidoctor {
	inputs.dir(project.extra["snippetsDir"]!!)
	dependsOn(tasks.test)
}
