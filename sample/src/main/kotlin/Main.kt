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

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.fleey.toggle.sample.SampleApp

fun main() = application {
  /**
   * Must give the window a state to remember, because toggle theme will use it to capture the window, and so on.
   * The `rememberWindowState` function is used to remember the state of the window.
   * The `size` parameter is used to set the initial size of the window.
   */
  val windowState = rememberWindowState(size = DpSize(420.dp, 720.dp))
  Window(
    state = windowState,
    onCloseRequest = ::exitApplication,
    title = "Theme Toggle"
  ) {
    /**
     * Call the `SampleApp` composable function to display the sample app.
     */
    SampleApp(window, windowState)
  }
}