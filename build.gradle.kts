plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.github.artyushov.idea-jmh-plugin"
version = "1.6.0"

repositories {
    mavenCentral()
}

intellij {
    version.set("2024.1.1")
    type.set("IC")

    plugins.set(listOf("java"))
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    patchPluginXml {
        sinceBuild.set("241")
        untilBuild.set("")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
