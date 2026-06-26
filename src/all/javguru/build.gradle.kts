plugins {
    alias(kei.plugins.extension)
}

yuzono {
    name = "Jav Guru"
    className = "JavGuru"
    versionCode = 47
    contentWarning = ContentWarning.NSFW
    libVersion = "14"
}

dependencies {
    implementation(project(":lib:streamwishextractor"))
    implementation(project(":lib:streamtapeextractor"))
    implementation(project(":lib:doodextractor"))
    implementation(project(":lib:mixdropextractor"))
    implementation(project(":lib:unpacker"))
    implementation(project(":lib:playlistutils"))
    implementation(project(":lib:javcoverfetcher"))
}
