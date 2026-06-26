plugins {
    alias(kei.plugins.extension)
}

yuzono {
    name = "AniDB"
    className = "AniDB"
    versionCode = 7
    contentWarning = ContentWarning.NSFW
    libVersion = "16"
}

dependencies {
    implementation(project(":lib:playlistutils"))
}
