plugins {
    `kotlin-dsl`
    id("com.ncorti.ktfmt.gradle") version "0.7.0"
}

repositories {
    mavenCentral()
}

ktfmt {
    kotlinLangStyle()
}