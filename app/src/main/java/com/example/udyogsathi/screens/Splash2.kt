package com.example.udyogsathi.screens

import android.net.Uri
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.udyogsathi.navigation.Routes
import com.example.udyogsathi.ui.theme.Darkgreen
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontFamily
import com.example.udyogsathi.ui.theme.Gradient2
import com.example.udyogsathi.ui.theme.gradientTextStyle

@Composable
fun Splash2(navController: NavHostController) {
    val videoUrl = "file:///android_asset/bgudyogsathi.mp4" // Make sure your video is in the assets folder
    val textList = listOf(
        "UdyogSathi a platform where you, as a wholesaler or retailer, can connect and communicate efficiently, helping both your business and others to grow.",
        "With UdyogSathi’s map feature, you can easily discover nearby wholesalers and retailers, making local connections more convenient.",
        "You can communicate effectively through chat, interact with an AI chatbot, know about latest offers from wholesaler or engage with potential retailers."
    )

    var currentTextIndex by remember { mutableStateOf(0) }

    // Change text every 5 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000) // Wait for 5 seconds
            currentTextIndex = (currentTextIndex + 1) % textList.size // Loop through texts
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background video
        VideoBackgroundPlayer(videoUrl)

        // Get Started Button
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f)) // Spacer to push button to the bottom

            Text(
                text = "UdyogSathi",
                fontFamily = FontFamily.SansSerif,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Display the current text
            Text(
                text = textList[currentTextIndex],
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 26.sp
                ),
                modifier = Modifier.padding(vertical = 6.dp)
            )

            // Circles indicator
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = 26.dp)
            ) {
                textList.indices.forEach { index ->
                    val color = if (index == currentTextIndex) Color.White else Color.Gray
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                    if (index < textList.lastIndex) {
                        Spacer(modifier = Modifier.size(8.dp)) // Adjust the size for spacing
                    }
                }
            }

            ElevatedButton(
                onClick = {
                    // Navigate to the login/register screen
                    navController.navigate(Routes.Login.routes) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Darkgreen)
            ) {
                Text(
                    text = "Get started",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}


@Composable
fun VideoBackgroundPlayer(videoUrl: String) {
    val context = LocalContext.current
    val player = remember { ExoPlayer.Builder(context).build() }

    // Prepare the media item
    val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
    player.setMediaItem(mediaItem)
    player.prepare()
    player.playWhenReady = true // Auto play
    player.repeatMode = ExoPlayer.REPEAT_MODE_ONE // Loop

    // Cleanup on dispose
    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    AndroidView(
        factory = { context ->
            AspectRatioFrameLayout(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                val playerView = PlayerView(context).apply {
                    this.player = player
                    useController = false // Set to true if you want to show controls
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    // Set to fill the player view while maintaining the aspect ratio
                    this.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL)
                }

                addView(playerView)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
