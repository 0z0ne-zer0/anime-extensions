plugins {
    alias(kei.plugins.extension)
}

yuzono {
    name = "Anime Srbija"
    className = "AnimeSrbija"
    versionCode = 24
    contentWarning = ContentWarning.SAFE
    libVersion = "14"
}

dependencies {
    implementation(project(":lib:filemoonextractor"))
}
