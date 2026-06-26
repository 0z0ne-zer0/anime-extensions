plugins {
    alias(kei.plugins.extension)
}

yuzono {
    name = "Movy"
    className = "Movy"
    versionCode = 1
    contentWarning = ContentWarning.NSFW
    libVersion = "16"
}

dependencies {
    implementation(project(":lib:playlistutils"))
}
