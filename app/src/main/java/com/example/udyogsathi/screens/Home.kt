package com.example.udyogsathi.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.udyogsathi.R
import com.example.udyogsathi.item_view.ThreadItem
import com.example.udyogsathi.navigation.Routes
import com.example.udyogsathi.ui.theme.Darkgreen
import com.example.udyogsathi.ui.theme.LightGreen
import com.example.udyogsathi.ui.theme.gradientTextStyle
import com.example.udyogsathi.utils.SharedPref
import com.example.udyogsathi.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import java.io.DataInput
import kotlin.time.Duration.Companion.seconds
@Composable
fun Home(navHostController: NavHostController) {

    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel()
    val threadsAndUsers by homeViewModel.threadsAndUsers.observeAsState(null)

    // Lottie animation
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.chatbotanimation))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientTextStyle.brush!!)
    ) {

        Text(
            text = "UdyogSathi", style = TextStyle(
                fontSize = 26.sp,
                color = Color.White
            ), modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 18.dp)
                .padding(start = 22.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.chaticon),
            contentDescription = "Chat",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 20.dp, top = 25.dp)
                .size(23.dp)
                .clickable {
                                  }
        )


    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 65.dp)
            .background(gradientTextStyle.brush!!)
            .clip(
                RoundedCornerShape(
                    topStart = 25.dp,
                    topEnd = 25.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
    )
    {


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

                LazyColumn {
                    items(threadsAndUsers ?: emptyList()) { pairs ->

                        ThreadItem(
                            thread = pairs.first,
                            users = pairs.second,
                            navHostController,
                            FirebaseAuth.getInstance().currentUser!!.uid
                        )
                    }
                }

                // Lottie Animation - Chat Bot
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(gradientTextStyle.brush!!, shape = CircleShape)
                            .clickable {
                                navHostController.navigate(Routes.ChatBot.routes) {
                                    popUpTo(navHostController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }

                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.BottomEnd

                ) {

                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier
                            .size(150.dp)
                            .padding(start = 40.dp)
                            .align(Alignment.BottomEnd)
                    )
                }

            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowHome() {
    // Home(navHostController = rememberNavController()) // Uncomment with actual NavController
}
