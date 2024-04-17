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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import com.fleey.toggle.ToggleEffectBox
import java.awt.Desktop
import java.net.URI
import kotlin.system.exitProcess

@Composable
fun SampleApp(
  window: ComposeWindow,
  windowState: WindowState
) {
  val isSystemDark = isSystemInDarkTheme()
  var isDark by remember { mutableStateOf(isSystemDark) }
  
  /**
   * The position where the effect starts.
   */
  var triggerPosition by remember { mutableStateOf(Offset.Zero) }
  
  val toggleTheme = { isDark = !isDark }
  
  /**
   * Update the trigger position when the icon button is clicked.
   */
  val onUpdateTriggerPosition: (Offset) -> Unit = { triggerPosition = it }
  
  /**
   * It's not important, just for fun, hope you enjoy it :).
   */
  val msgList by remember { mutableStateOf(mutableListOf<Msg>()) }
  var toggleCount by remember { mutableStateOf(0) }
  val onAddToggleCount: () -> Unit = {
    if (toggleCount == 32) msgList.clear()
    if (toggleCount > 32) exitProcess(0)
    msgList.add(0, getMsg(toggleCount))
    toggleCount++
  }
  
  LaunchedEffect(Unit) { onAddToggleCount() }
  
  AppTheme(isDark) {
    SampleScaffold(isDark, toggleTheme, onUpdateTriggerPosition, onAddToggleCount) {
      SampleContent(msgList)
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
}

@Composable
fun SampleScaffold(
  isDark: Boolean,
  toggleTheme: () -> Unit,
  onUpdateTriggerPosition: (Offset) -> Unit,
  onAddToggleCount: () -> Unit,
  content: @Composable (PaddingValues) -> Unit
) {
  Scaffold(
    topBar = { SampleTopBar(isDark, toggleTheme, onUpdateTriggerPosition, onAddToggleCount) },
    content = content
  )
}

@Composable
fun SampleTopBar(
  isDark: Boolean,
  toggleTheme: () -> Unit,
  onUpdateTriggerPosition: (Offset) -> Unit,
  onAddToggleCount: () -> Unit
) {
  val title = if (isDark) "Dark Mode" else "Light Mode"
  val icon = if (isDark) Icons.Rounded.LightMode else Icons.Rounded.DarkMode
  
  TopAppBar(
    title = { Text(title) },
    actions = {
      Box {
        IconButton(
          modifier = Modifier.onGloballyPositioned {
            /**
             * Get the center position of the icon button, then update the TriggerPosition.
             * And you can use [androidx.compose.ui.input.pointer.pointerInput] to get the position of the mouse click, then calculate the click position.
             * But I think this is enough for this sample, so more implementation is up to you.
             */
            onUpdateTriggerPosition(it.boundsInWindow().center)
          },
          onClick = {
            toggleTheme()
            onAddToggleCount()
          }
        ) {
          Icon(icon, "Toggle Theme")
        }
      }
    },
    elevation = 0.dp
  )
}

@Composable
fun SampleContent(
  msgList: List<Msg>
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(24.dp),
  ) {
    Surface(
      Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.surface),
      elevation = 1.dp
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Surface(shape = CircleShape) {
          /**
           * It's avatar of Fleey, the author :D.
           * Yep, I'm a furry, did you like it?
           * You can replace it with your avatar.
           */
          Image(
            modifier = Modifier.size(128.dp),
            painter = painterResource("dev.png"),
            contentDescription = "Developer Avatar",
            contentScale = ContentScale.Crop
          )
        }
        
        Spacer(Modifier.height(12.dp))
        Text("Fleey", style = MaterialTheme.typography.h5)
        Spacer(Modifier.height(12.dp))
        
        Button(
          modifier = Modifier.height(36.dp),
          onClick = { openUrlInBrowser("https://github.com/MultiWolf/themeToggle-like-Telegram") },
          elevation = ButtonDefaults.elevation(0.dp)
        ) {
          Text("Check it on GitHub")
        }
      }
    }
    
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
      msgList.forEach { MsgItem(it) }
    }
  }
}

data class Msg(
  val content: String,
  val color: Color? = null
)

@Composable
fun MsgItem(msg: Msg) {
  Surface(
    modifier = Modifier.fillMaxWidth(),
    color = MaterialTheme.colors.surface,
    elevation = 1.dp
  ) {
    Text(
      msg.content,
      modifier = Modifier.padding(8.dp),
      color = msg.color ?: MaterialTheme.colors.onBackground,
      textAlign = TextAlign.Center
    )
  }
}

/**
 * Get the message by the toggle count.
 * It's just for fun, you can remove it if you don't like it -_-
 */
private fun getMsg(toggleCount: Int): Msg {
  return when (toggleCount) {
    0 -> Msg("Now, try clicking the icon in the top right corner to see the effect!")
    1 -> Msg("God, my eyes!! Look what you've done. Switch back quickly!", Color.Blue)
    2 -> Msg("Oh, that's better. But I'd love to see that cool again, so click it again!")
    3 -> Msg("No!! Let's switch back. I can't take it anymore!")
    4 -> Msg("That's it. Let's not switch anymore. It's comfortable, isn't it!")
    5 -> Msg(
      "You're really enjoying this, aren't you? I'm not sure I can take it anymore.",
      Color.Red
    )
    
    6 -> Msg("All right, don't click it anymore. I'm going to close the window.")
    7 -> Msg("I'm serious. I'm going to close the window.", Color.Red)
    8 -> Msg("So, you're not going to stop, are you?")
    9 -> Msg("I'm done with you. I'm out of here!")
    10 -> Msg("However you click, I won't respond. I'm leaving!")
    16 -> Msg("Why are you still here? I've already left!", Color.Magenta)
    24 -> Msg("I'm not here. I'm not here. I'm not here.", Color.Green)
    32 -> Msg("You're so boring, I'll let the system close the window for you!", Color.Red)
    else -> Msg("Goodbye!")
  }
}

fun openUrlInBrowser(url: String) {
  try {
    if (Desktop.isDesktopSupported()) {
      val desktop = Desktop.getDesktop()
      if (desktop.isSupported(Desktop.Action.BROWSE)) {
        desktop.browse(URI(url))
      }
    }
  } catch (e: Exception) {
    e.printStackTrace()
  }
}