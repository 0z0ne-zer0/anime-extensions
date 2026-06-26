plugins {
    alias(kei.plugins.extension)
}

yuzono {
    name = "Anime Fire"
    className = "AnimeFire"
    versionCode = 10
    contentWarning = ContentWarning.SAFE
    libVersion = "16"
}

dependencies {
    implementation("com.github.komikku-app.nanohttpd:nanohttpd:gradle-upgrade-SNAPSHOT")
}
