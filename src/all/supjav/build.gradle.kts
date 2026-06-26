plugins {
    alias(kei.plugins.extension)
}

yuzono {
    name = "SupJav"
    className = "SupJavFactory"
    versionCode = 31
    contentWarning = ContentWarning.NSFW
    libVersion = "14"
}

dependencies {
    implementation(project(":lib:streamtapeextractor"))
    implementation(project(":lib:streamwishextractor"))
    implementation(project(":lib:voeextractor"))
    implementation(project(":lib:playlistutils"))
}
