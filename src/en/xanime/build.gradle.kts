plugins {
    alias(kei.plugins.extension)
}

yuzono {
    name = "XAnime"
    className = "XAnime"
    versionCode = 4
    contentWarning = ContentWarning.NSFW
    libVersion = "16"
}

dependencies {
    implementation(project(":lib:playlistutils"))
}
