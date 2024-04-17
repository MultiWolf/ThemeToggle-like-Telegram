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

package com.fleey.toggle.util

import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.window.WindowState
import java.awt.*

/**
 * Use singleton object to capture the window as an image and reduce the memory usage.
 * You can also use [ImageComposeScene] to capture all @Composable as an image,
 * but I'm not sure if it can capture the window, and I'm lazy to test it.
 * And you need give width and height to [ImageComposeScene], that's ugly.
 * If you try it, please let me know the result, thanks!
 * @see [ImageComposeScene]
 */
internal object CaptureUtil {
  /**
   * Use [Robot] to capture the screen.
   */
  private val robot by lazy { Robot() }
  
  /**
   * Capture the window as an image.
   * Don't forget to allow the record permission to capture the screen.
   * And I don't suggest you to use coroutine to capture, it may cause the screen flicker, or other issues.
   * @param position the position of the window.
   * @param windowState the window state.
   * @param marginBar the margin bar to cover the window, e.g. 28 for macOS, 0 for CustomWindow.
   * @param hideCursor whether to hide the cursor when capturing.
   * @return the [ImageBitmap](captured image).
   */
  fun captureWindowAsImage(
    position: Point,
    windowState: WindowState,
    marginBar: Int,
    hideCursor: Boolean = true
  ): ImageBitmap {
    val width = windowState.size.width.value.toInt()
    val height = windowState.size.height.value.toInt() - marginBar
    
    /**
     * Move the cursor to a position that is not visible, I use a stinky number to move it out of the screen.
     * Use [MouseInfo.getPointerInfo] to get the current position.
     */
    val moveDistance = 114514
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val originMousePosition = MouseInfo.getPointerInfo().location
    
    /**
     * Create a rectangle to capture the window.
     */
    val captureRect = Rectangle(
      position.x,
      position.y + marginBar,
      width,
      height
    )
    
    try {
      if (hideCursor) robot.mouseMove(
        screenSize.width + moveDistance,
        screenSize.height + moveDistance
      )
      
      return robot.createScreenCapture(captureRect).toComposeImageBitmap()
    } finally {
      /**
       * Move the cursor back to the original position.
       */
      if (hideCursor) robot.mouseMove(originMousePosition.x, originMousePosition.y)
    }
  }
}