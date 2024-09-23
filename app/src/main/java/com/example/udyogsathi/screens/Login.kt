package com.example.udyogsathi.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.udyogsathi.R // Make sure to import R from your app package
import com.example.udyogsathi.navigation.Routes
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

    // Background image
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg2), // Assuming bg.jpg is in the drawable folder
            contentDescription = null, // Content description is null for decorative images
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.TopCenter
        )

        // Content of the login screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 350.dp)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Login",
                style = TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 34.sp
                )
            )

            Box(modifier = Modifier.height(25.dp))

            OutlinedTextField(
                value = email ,
                onValueChange = {email = it},
                label = { Text(text = "  Email  ",
                color = Color.White
                )},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true ,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.LightGray, // Set border color when focused
                    unfocusedBorderColor = Color.Gray, // Set border color when not focused
                    cursorColor = Color.White // Set cursor color
                )

            )

            Box(modifier = Modifier.height(7.dp))

            OutlinedTextField(
                value = password ,
                onValueChange = {password = it},
                label = { Text(text = "  Password  ",
                    color = Color.White)},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.LightGray, // Set border color when focused
                    unfocusedBorderColor = Color.Gray, // Set border color when not focused
                    cursorColor = Color.White // Set cursor color
                )
            )

            Box(modifier = Modifier.height(30.dp))

            ElevatedButton(onClick = {

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(context,"Please provide all fields",Toast.LENGTH_SHORT).show()
                }else {
                    authViewModel.login(email, password, context)
                }

            } ,colors = ButtonDefaults.buttonColors(Color.White),
                modifier = Modifier.fillMaxWidth()) {
                Text(text = "Login",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(vertical = 6.dp),


                )
            }



            TextButton(onClick = {
                navHostController.navigate(Routes.Register.routes){
                    popUpTo(navHostController.graph.startDestinationId)
                    launchSingleTop=true
                }

            },

                colors = ButtonDefaults.textButtonColors(

                    contentColor = Color.LightGray
                ))
            {
                Text(text = "New User? Create Account",
                    style = TextStyle(
                        fontSize = 16.sp
                    ))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginView() {
    // Login()
}
