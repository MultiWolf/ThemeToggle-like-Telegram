import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  kotlin("jvm")
  id("org.jetbrains.compose")
}

group = "com.fleey.toggle"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  google()
}

dependencies {
  implementation(compose.desktop.currentOs)
  implementation(compose.materialIconsExtended)
}

compose.desktop {
  application {
    mainClass = "MainKt"
    
    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "ThemeToggle-like-Telegram"
      packageVersion = "1.0.0"
    }
  }
}
