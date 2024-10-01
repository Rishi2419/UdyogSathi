package com.example.udyogsathi.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.udyogsathi.R // Make sure to import R from your app package
import com.example.udyogsathi.navigation.Routes
import com.example.udyogsathi.ui.theme.Darkgreen
import com.example.udyogsathi.ui.theme.Gradient1
import com.example.udyogsathi.ui.theme.Gradient2
import com.example.udyogsathi.ui.theme.LightGreen
import com.example.udyogsathi.ui.theme.gradientTextStyle
import com.example.udyogsathi.utils.SharedPref
import com.example.udyogsathi.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navHostController: NavHostController) {

    val authViewModel : AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState()
    val error by authViewModel.error.observeAsState()

    LaunchedEffect(firebaseUser){
        if (firebaseUser != null){
            navHostController.navigate(Routes.BottomNav.routes){
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop=true
            }
        }
    }

    val context = LocalContext.current

    error?.let {
        Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
    }

    var email by remember {
        mutableStateOf("")
    }
    var password  by remember {
        mutableStateOf("")
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logincircle),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.BottomCenter
        )

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loginanimation))
        val progress by animateLottieCompositionAsState(composition)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp), // Removed the padding from top and added overall padding
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // Space the content evenly between top and bottom
        ) {

            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 120.dp),
                contentScale = ContentScale.Crop
            )
            // Top Section (Animation or Logo)
            Column(
                //modifier = Modifier.padding(top = 10.dp), // You can adjust the top padding as needed
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gradient2
                )

                Spacer(modifier = Modifier.height(25.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = " Email ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Gradient2,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Gradient2,
                        focusedLabelColor = Gradient2,
                        unfocusedLabelColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(7.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = " Password ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Gradient2,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Gradient2,
                        focusedLabelColor = Gradient2,
                        unfocusedLabelColor = Color.Gray
                    )
                )
            }

            // Bottom Section (Buttons)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom // Align at the bottom
            ) {

                ElevatedButton(
                    onClick = {
                        if (email.isEmpty() || password.isEmpty()) {
                            Toast.makeText(context, "Please provide all fields", Toast.LENGTH_SHORT).show()
                        } else {
                            authViewModel.login(email, password, context)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Darkgreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Login",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp)) // Small gap between the buttons

                TextButton(
                    onClick = {
                        navHostController.navigate(Routes.Register.routes) {
                            popUpTo(navHostController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text(
                        text = "New User? Create Account",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }
        }
    }

    }


@Preview(showBackground = true)
@Composable
fun LoginView() {
    // Login()
}
