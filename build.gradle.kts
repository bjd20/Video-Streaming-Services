plugins {
    java
    id("org.springframework.boot") version "3.5.10" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

allprojects {
    group = "com.videostreaming"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }
}