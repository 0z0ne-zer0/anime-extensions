plugins {
    alias(kei.plugins.extension)
}

yuzono {
    name = "HDFilmCehennemi"
    className = "HDFilmCehennemi"
    versionCode = 34
    contentWarning = ContentWarning.NSFW
    libVersion = "14"
}

dependencies {
    implementation(project(":lib:unpacker"))
    implementation(project(":lib:playlistutils"))
}
