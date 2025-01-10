plugins {
    id("java")
    // Maven Publish Plugin für Artefaktveröffentlichung
    id("maven-publish")
    id("java-library")
}

group = "de.relaxogames"
version = "inDev-1.5"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // Implementierungs-Abhängigkeiten
    implementation("de.relaxogames:snorlax-log:1.7")
    implementation("org.yaml:snakeyaml:2.0")
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components["java"]) // Bindet das Java-Artefakt ein

            groupId = "de.relaxogames"
            artifactId = "LingoAPI"
            version = "inDev-1.5"

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
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "local"
            url = uri(layout.buildDirectory.dir("maven-repo")) // Lokales Test-Repository
        }
    }
}

