plugins {
    alias(kei.plugins.extension)
}

yuzono {
    name = "Animes Digital"
    className = "AnimesDigital"
    versionCode = 10
    contentWarning = ContentWarning.SAFE
    libVersion = "16"
}

dependencies {
    implementation(project(":lib:bloggerextractor"))
    implementation(project(":lib:unpacker"))
}
