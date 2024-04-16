/*
 * Copyright (c) 2024-present. Fleey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fleey.sample

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppTheme(
  isDark: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colors = if (isDark) darkColorPalette else lightColorPalette
  
  MaterialTheme(
    colors = colors,
    content = content
  )
}

private val lightColorPalette = lightColors(
  background = Color(0xFFF2F3F5),
  onBackground = Color(0xFFABB1B8),
  secondary = Color(0xFF5765F2),
  primary = Color(0xFF5765F2),
  surface = Color(0xFFFFFFFF),
  onSurface = Color(0xFF4F5660),
  onPrimary = Color(0xFFFFFFFF),
  onSecondary = Color(0xFF5765F2),
  secondaryVariant = Color(0xFFF2F3F5),
  primaryVariant = Color(0xFF575860)
)

private val darkColorPalette = darkColors(
  background = Color(0xFF1F2225),
  onBackground = Color(0xFFABB1B8),
  secondary = Color(0xFF5765F2),
  primary = Color(0xFF5765F2),
  surface = Color(0xFF1F2225),
  onSurface = Color(0xFFABB1B8),
  onPrimary = Color(0xFFFFFFFF),
  onSecondary = Color(0xFF1F2225),
  secondaryVariant = Color(0xFF2C2F32),
  primaryVariant = Color(0xFFDCDCDC)
)