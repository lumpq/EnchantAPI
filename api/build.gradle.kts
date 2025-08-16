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
            // `api` 모듈의 java 컴포넌트를 사용합니다.
            from(components["java"])
        }
    }
    // 이 repositories 블록은 로컬 배포에서는 필요하지 않습니다.
    // 원격 GitHub Packages 배포 시에만 사용하세요.
    repositories {
        mavenLocal()
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}