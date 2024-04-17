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

package com.fleey.toggle

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.WindowState
import com.fleey.toggle.util.CaptureUtil.captureWindowAsImage
import java.awt.Point
import kotlin.math.sqrt

/**
 * A composable that creates a toggle effect box.
 * @param isDark the dark mode state.
 * @param window the window to capture.
 * @param windowState the window state, use it to get the window size, then capture the window.
 * @param coverMarginBar the margin bar to cover the window, e.g. about 28 for macOS, 0 for CustomWindow.
 * @param triggerPosition the trigger position to start the effect.
 * @param animDurationMillis the animation duration in milliseconds.
 * @param animEasing the animation easing.
 * @param modifier the modifier.
 * @see captureWindowAsImage
 */
@Composable
fun ToggleEffectBox(
  isDark: Boolean,
  window: ComposeWindow,
  windowState: WindowState,
  coverMarginBar: Int = 0,
  triggerPosition: Offset = Offset.Zero,
  animDurationMillis: Int = 1145,
  animEasing: Easing = FastOutSlowInEasing,
  modifier: Modifier = Modifier.fillMaxSize()
) {
  var isDarkMode by remember { mutableStateOf(isDark) }
  
  /**
   * windowPosition is used to get the window position.
   */
  var windowPosition by remember { mutableStateOf(Point(0, 0)) }
  
  /**
   * cover is used to store the captured image.
   */
  var cover by remember { mutableStateOf<ImageBitmap?>(null) }
  
  /**
   * triggerCount is used to trigger control the animation,
   * because [animateFloatAsState] can‘t directly change the current targetValue without an animation process,
   * like [androidx.compose.animation.core.Animatable] s snapTo() function.
   */
  var triggerCount by remember { mutableStateOf(0) }
  
  /**
   * triggerAnim is used to trigger the animation.
   */
  var triggerAnim by remember { mutableStateOf(false) }
  
  /**
   * animProgress is used to control the radius of the canvas.
   */
  val animProgress by animateFloatAsState(
    targetValue = (triggerCount + 1).toFloat(),
    animationSpec = tween(animDurationMillis, 0, animEasing),
    finishedListener = {
      if (it == (triggerCount + 1).toFloat()) {
        cover = null
        triggerAnim = false
      }
    }
  )
  
  LaunchedEffect(windowState.position) { windowPosition = window.location }
  
  /**
   * When the dark mode state changes, capture the window and trigger the animation.
   */
  LaunchedEffect(isDark) {
    if (isDarkMode != isDark) {
      isDarkMode = isDark
      cover =
        captureWindowAsImage(window.location, windowState, coverMarginBar)
      triggerCount++
      triggerAnim = true
    }
  }
  
  /**
   * Draw the canvas.
   */
  Canvas(Modifier then (modifier)) {
    if (triggerAnim) {
      /**
       * Calculate the radius of the canvas.
       * [isDark] is used to control the direction of the animation.
       */
      val radius =
        sqrt(size.width * size.width + size.height * size.height) * if (isDarkMode) animProgress - triggerCount else (triggerCount + 1) - animProgress
      
      drawIntoCanvas { canvas ->
        /**
         * Draw the oval path.
         */
        val clipPath = Path().apply { addOval(Rect(triggerPosition, radius)) }
        
        /**
         * Clip the canvas.
         * [isDark] is used to control the clip operation.
         * If [isDark] is true, use [ClipOp.Difference] to clip the canvas, it's means show the outside of the path.
         * If [isDark] is false, use [ClipOp.Intersect] to clip the canvas, it's means show the inside of the path.
         */
        canvas.clipPath(clipPath, if (isDarkMode) ClipOp.Difference else ClipOp.Intersect)
        
        /**
         * Draw the cover image.
         */
        cover?.let {
          canvas.drawImageRect(
            it,
            paint = Paint(),
            srcSize = IntSize(it.width, it.height),
            dstSize = IntSize(size.width.toInt(), size.height.toInt())
          )
        }
      }
    }
  }
}