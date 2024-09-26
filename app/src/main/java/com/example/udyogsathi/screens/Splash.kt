package com.example.udyogsathi.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.udyogsathi.R
import com.example.udyogsathi.navigation.Routes
import com.example.udyogsathi.ui.theme.Darkgreen
import com.example.udyogsathi.ui.theme.LightGreen
import com.example.udyogsathi.ui.theme.black
import com.example.udyogsathi.ui.theme.gradientTextStyle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun Splash(navController: NavHostController){

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Darkgreen)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(id = R.drawable.logo), contentDescription ="",
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .size(160.dp)

            )

                Text(
                    text = "UdyogSathi",
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            Text(
                    text = "Start-Connect-Grow",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Thin,
                    fontSize = 16.sp,
                    color = Color.LightGray,


                )



        }

    }



//    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
//
//        val(image)= createRefs()
//        Image(painter = painterResource(id = R.drawable.logo)  , contentDescription = "logo" ,
//
//            modifier = Modifier.constrainAs(image){
//            top.linkTo(parent.top)
//            bottom.linkTo(parent.bottom)
//            start.linkTo(parent.start)
//            end.linkTo(parent.end)
//        })
//
//    }

    LaunchedEffect(key1 = true){
        delay(1000) // Increase the delay duration to 5000 milliseconds (5 seconds)

//        if (FirebaseAuth.getInstance().currentUser != null) {
//            navController.navigate(Routes.BottomNav.routes) {
//                popUpTo(navController.graph.startDestinationId)
//                launchSingleTop = true
//            }
//
//        }
        if (FirebaseAuth.getInstance().currentUser != null) {
            //takes to home screen
            navController.navigate(Routes.BottomNav.routes) {
                // clears all backstack
                popUpTo(navController.graph.startDestinationId){
                    //Splash screen is removed
                    inclusive = true
                }
            }

        }
        else{
            navController.navigate(Routes.Login.routes){
                popUpTo(navController.graph.startDestinationId){
                    inclusive = true
                }
            }
        }




    }

}

