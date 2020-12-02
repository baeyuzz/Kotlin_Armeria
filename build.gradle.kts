import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    application
    kotlin("jvm") version "1.4.10"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1" apply false

    id ("org.springframework.boot") version "2.3.2.RELEASE"
    id ("io.spring.dependency-management") version "1.0.8.RELEASE"

    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.jpa") version "1.3.72"
    kotlin("kapt") version "1.4.10"
}

repositories {
    mavenCentral()
}

dependencies {


    implementation("org.mongodb","mongo-java-driver","3.12.7")
    implementation("com.google.code.gson","gson","2.8.1")
    implementation("org.springframework.data","spring-data-mongodb","1.8.6.RELEASE")



    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.querydsl:querydsl-jpa:4.2.1")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.7.0")
    kapt("com.querydsl:querydsl-apt:4.2.2:jpa")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor(group = "com.querydsl", name = "querydsl-apt", classifier = "jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test")



    implementation ("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage", "junit-vintage-engine")
    }

    implementation ("com.drewnoakes", "metadata-extractor" ,"2.15.0")


    implementation(platform("com.linecorp.armeria:armeria-bom:1.2.0"))
    implementation("com.linecorp.armeria:armeria-kotlin")
    // kotlin coroutine 사용

    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
    runtimeOnly( "org.slf4j:log4j-over-slf4j:1.7.30")

    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.jetbrains.kotlin", "kotlin-test-junit5", "1.4.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
    implementation("net.javacrumbs.json-unit","json-unit-fluent","1.13.0")
    testImplementation("org.assertj","assertj-core", "3.18.1")

    implementation("org.mongodb" , "mongodb-driver" ,"3.6.3")

}

application {
    mainClassName = "example.armeria.server.annotated.kotlin.MainKt"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
