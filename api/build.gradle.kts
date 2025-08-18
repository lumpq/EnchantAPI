plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.lumpq126"
            artifactId = "enchantapi"
            version = "1.0.0"
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    // javadoc.jar, sources.jar 같이 생성되도록 설정
    withJavadocJar()
    withSourcesJar()
}

// javadoc 태스크 커스터마이징 (옵션)
tasks.javadoc {
    isFailOnError = false // 경고 때문에 빌드 실패하지 않게
    options.encoding = "UTF-8"
    (options as StandardJavadocDocletOptions).apply {
        addStringOption("Xdoclint:none", "-quiet")
    }
}
