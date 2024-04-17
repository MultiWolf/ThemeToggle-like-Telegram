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

package com.fleey.toggle.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.window.WindowState
import com.fleey.toggle.ToggleEffectBox

/**
 * A simple sample to show how to use the [ToggleEffectBox]
 * And here I provide a better sample [SampleApp]
 * @param window the window to capture.
 * @param windowState the window state, use it to get the window size, then capture the window.
 * @see ToggleEffectBox
 * @see SampleApp
 */
@Composable
fun SimpleSample(
  window: ComposeWindow,
  windowState: WindowState
) {
  val isSystemDark = isSystemInDarkTheme()
  var isDark by remember { mutableStateOf(isSystemDark) }
  var triggerPosition by remember { mutableStateOf(Offset.Zero) }
  
  val toggleTheme = { isDark = !isDark }
  
  /**
   * when isDark changed, [ToggleEffectBox] will be triggered to show the effect.
   */
  AppTheme(isDark) {
    Box(
      Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
        /**
         * Update the trigger position when the icon button is clicked.
         * And toggle the theme when the box is clicked.
         * But it's not fit for most scenarios,
         * So, here I provide a better way to update the trigger position.
         * @see SampleApp
         */
        .pointerInput(Unit) {
          detectTapGestures {
            triggerPosition = it
            toggleTheme()
          }
        }
    ) {
    }
  }
  /**
   * You should put this codes below others, so it can be drawn on top of them.
   * And I set the coverMarginBar to 28, because the system(macOS) window bar height about 28.
   * You can change it by your system's top bar height.
   */
  ToggleEffectBox(
    isDark,
    window,
    windowState,
    28,
    triggerPosition
  )
}