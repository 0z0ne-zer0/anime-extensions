plugins {
    alias(kei.plugins.extension)
}

yuzono {
    name = "KickAssAnime"
    className = "KickAssAnime"
    versionCode = 62
    contentWarning = ContentWarning.SAFE
    libVersion = "14"
}

dependencies {
    implementation(project(":lib:cryptoaes"))
    implementation(project(":lib:playlistutils"))
}
