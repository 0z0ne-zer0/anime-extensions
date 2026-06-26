plugins {
    alias(kei.plugins.extension)
}

yuzono {
    name = "AniHQ"
    className = "AniHQ"
    versionCode = 3
    contentWarning = ContentWarning.NSFW
    libVersion = "16"
}

dependencies {
    implementation(project(":lib:voeextractor"))
    implementation(project(":lib:pixeldrainextractor"))
}
