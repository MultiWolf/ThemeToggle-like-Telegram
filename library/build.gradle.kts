plugins {
  kotlin("jvm")
  id("org.jetbrains.compose")
}

dependencies {
  implementation(compose.desktop.common)
}

kotlin {
  jvmToolchain(17)
}