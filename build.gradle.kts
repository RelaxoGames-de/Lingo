plugins {
    id("java")
    id("maven-publish")
    id("java-library")
}

group = "de.relaxogames"
version = "inDev-1.7-RC01"

repositories {
    mavenCentral()
    mavenLocal()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(17)
}

dependencies {
    implementation("org.yaml:snakeyaml:2.0")
    implementation("net.kyori:adventure-api:4.18.0")
    implementation("net.kyori:adventure-text-serializer-gson:4.18.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.18.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.2")
    implementation("com.zaxxer:HikariCP:5.0.1")
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components["java"])

            groupId = "de.relaxogames"
            artifactId = "LingoAPI"
            version = "inDev-1.7-RC01"

            pom {
                name.set("Lingo")
                description.set("Die zentrale Bibliothek für den Umgang mit Sprachdateien")
                url.set("https://github.com/RelaxoGames-de/Lingo.git")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/RelaxoGames-de/Lingo.git")
                    developerConnection.set("scm:git:ssh://github.com/RelaxoGames-de/Lingo.git")
                    url.set("https://github.com/RelaxoGames-de/Lingo")
                }

                developers {
                    developer {
                        id.set("DevTex")
                        name.set("SnorlaxLabs")
                        email.set("support@snorlaxlabs.de")
                        organizationUrl.set("http://snorlaxlabs.de")
                        description.set("Copyright © 2025 SnorlaxLabs. All rights reserved.")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "local"
            url = uri(layout.buildDirectory.dir("maven-repo"))
        }
    }
}
