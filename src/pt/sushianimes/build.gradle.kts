plugins {
    alias(kei.plugins.extension)
}

yuzono {
    name = "SushiAnimes"
    className = "SushiAnimes"
    versionCode = 2
    contentWarning = ContentWarning.NSFW
    libVersion = "14"
}

dependencies {
    implementation(project(":lib:bloggerextractor"))
}
