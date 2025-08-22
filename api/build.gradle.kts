import org.gradle.external.javadoc.StandardJavadocDocletOptions

plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.vanniktech.maven.publish") version "0.34.0"
}

group = "io.github.snowyblossom126"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

// Javadoc 설정
tasks.withType<Javadoc> {
    isFailOnError = false
    options.encoding = "UTF-8"
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

afterEvaluate {
    tasks.named("generateMetadataFileForMavenPublication") {
        dependsOn(tasks.named("plainJavadocJar"))
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates("io.github.snowyblossom126", "enchant-api", version.toString())

    pom {
        name.set("EnchantAPI")
        description.set("EnchantAPI is a library that provides API support for Minecraft plugins.")
        url.set("https://github.com/snowyblossom126/EnchantAPI")

        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("snowyblossom126")
                name.set("SnowyBlossom126")
            }
        }

        scm {
            url.set("https://github.com/snowyblossom126/EnchantAPI")
            connection.set("scm:git:https://github.com/snowyblossom126/EnchantAPI.git")
        }
    }
}

signing {
    useGpgCmd()
}
