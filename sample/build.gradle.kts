import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  kotlin("jvm")
  id("org.jetbrains.compose")
}

group = "com.fleey.toggle.sample"
version = "1.0-SNAPSHOT"

dependencies {
  implementation(compose.desktop.currentOs)
  implementation(compose.materialIconsExtended)
  implementation(project(":library"))
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
