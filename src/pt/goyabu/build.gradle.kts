plugins {
    alias(kei.plugins.extension)
}

yuzono {
    name = "Goyabu"
    className = "Goyabu"
    versionCode = 5
    contentWarning = ContentWarning.NSFW
    libVersion = "14"
}

dependencies {
    implementation(project(":lib:bloggerextractor"))
}
