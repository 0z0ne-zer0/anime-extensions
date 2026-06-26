plugins {
    alias(kei.plugins.extension)
}

yuzono {
    name = "Doramogo"
    className = "Doramogo"
    versionCode = 21
    contentWarning = ContentWarning.SAFE
    libVersion = "14"
}

dependencies {
    implementation(project(":lib:dailymotionextractor"))
    implementation(project(":lib:googledriveextractor"))
    implementation(project(":lib:okruextractor"))
    implementation(project(":lib:playlistutils"))
    implementation(project(":lib:unpacker"))
}
