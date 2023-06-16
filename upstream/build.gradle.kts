plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    api("org.jspecify:jspecify:0.3.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}
