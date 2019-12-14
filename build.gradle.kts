import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.jpa") version "1.3.50"
    id("org.springframework.boot") version "2.2.0.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("org.flywaydb.flyway") version "6.0.8"
    kotlin("jvm") version "1.3.50"
    kotlin("plugin.spring") version "1.3.50"
    kotlin("plugin.allopen") version "1.3.50"
    kotlin("kapt") version "1.3.50"
}

group = "com.morningbees"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mustache")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // Log
    implementation("net.logstash.logback:logstash-logback-encoder:6.2")
    implementation("ch.qos.logback.contrib:logback-json-classic:0.1.5")
    implementation("ch.qos.logback.contrib:logback-jackson:0.1.5")

    // Mysql
    implementation("mysql:mysql-connector-java:5.1.47")

    // JWT
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    // rest API
    implementation("org.apache.httpcomponents:httpclient:4.5")

    // Flyway
    implementation("org.flywaydb:flyway-core")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.flywaydb.flyway-test-extensions:flyway-spring5-test:6.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.3.50")
    testImplementation("org.assertj:assertj-core:3.13.2")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

flyway {}
