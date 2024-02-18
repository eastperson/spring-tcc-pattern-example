apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

plugins {
    kotlin("plugin.jpa") apply false
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}