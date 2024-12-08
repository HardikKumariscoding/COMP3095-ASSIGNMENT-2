plugins {
	java
	id("org.springframework.boot") version "3.4.0"
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
	extra["springCloudVersion"] = "2024.0.0-RC1"
//extra["springCloudVersion"] = "2023.0.3"
//extra["resilience4jVersion"]="0.17.0"


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.cloud:spring-cloud-starter-gateway")
	implementation ("io.micrometer:micrometer-registry-prometheus")
	annotationProcessor("org.projectlombok:lombok")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
	implementation ("com.github.loki4j:loki-logback-appender:1.4.1")
//	implementation ("org.keycloak:keycloak-spring-boot-starter:21.1.0")
	//	implementation("io.github.resilience4j:resilience4j-spring-boot3:2.0.2")
//	implementation("io.github.resilience4j:resilience4j-spring-boot2:0.17.0")
//	implementation("org.springframework.cloud:spring-cloud-starter-netflix-hystrix")
//	implementation ("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
//	implementation ("org.springframework.boot:spring-boot-starter-security")
//	implementation ("org.keycloak:keycloak-spring-boot-starter:22.0.0")
//	implementation ("org.keycloak:keycloak-admin-client:22.0.0")

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
