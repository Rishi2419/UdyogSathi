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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.example.udyogsathi.ui.theme.MediumGreen
import com.example.udyogsathi.utils.SharedPref
import com.example.udyogsathi.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import java.io.DataInput
import kotlin.time.Duration.Companion.seconds
@Composable
fun Home(navHostController: NavHostController){

    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel()
    val threadsAndUsers by homeViewModel.threadsAndUsers.observeAsState(null)

    // Lottie animation
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.chatbotanimation))

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn {

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Darkgreen)
                        .padding(vertical = 15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier.size(45.dp)
                    )
                }
            }

            items(threadsAndUsers ?: emptyList()) { pairs ->

                ThreadItem(
                    thread = pairs.first,
                    users = pairs.second,
                    navHostController,
                    FirebaseAuth.getInstance().currentUser!!.uid
                )
            }
        }

        // Lottie Animation - Bottom Right Corner
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)   // Align to the bottom right corner
                .padding(16.dp) ,
            contentAlignment = Alignment.BottomEnd// Padding to keep it above the nav bar
        ) {
            // Small Circle
            Box(
                modifier = Modifier
                    .size(60.dp)                  // Small circle size
                    .background(MediumGreen, shape = CircleShape)
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
                .align(Alignment.BottomEnd) ,  // Align to the bottom right corner
            contentAlignment = Alignment.BottomEnd
                // Padding to keep it above the nav bar
        ) {

            // Large Lottie Animation
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever, // Loop the animation
                modifier = Modifier
                    .size(150.dp)
                    .padding(start = 40.dp)// Larger Lottie animation size
                    .align(Alignment.BottomEnd)
                    // Optional vertical offset for better positioning
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ShowHome() {
    // Home(navHostController = rememberNavController()) // Uncomment with actual NavController
}
