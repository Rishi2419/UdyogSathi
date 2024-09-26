package com.example.udyogsathi.screens

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.udyogsathi.R
import com.example.udyogsathi.navigation.Routes
import com.example.udyogsathi.ui.theme.Darkgreen
import com.example.udyogsathi.ui.theme.LightGreen
import com.example.udyogsathi.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(navHostController: NavHostController){

    var email by remember {
        mutableStateOf("")
    }
    var password  by remember {
        mutableStateOf("")
    }
    var name  by remember {
        mutableStateOf("")
    }
    var userName  by remember {
        mutableStateOf("")
    }
    var bio  by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }


    val authViewModel : AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)
    //Launcher for selecting img

    val permissionToRequest = if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
        android.Manifest.permission.READ_MEDIA_IMAGES
    }else android.Manifest.permission.READ_EXTERNAL_STORAGE

    val context = LocalContext.current


    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
        uri : Uri? ->
        imageUri = uri
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()){

                isGranted : Boolean ->
            if (isGranted){
                Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
    }
    
    LaunchedEffect(firebaseUser){
        if (firebaseUser != null){
            navHostController.navigate(Routes.BottomNav.routes){
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop=true
            }
        }
    }





    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Darkgreen)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logincircle), // Assuming bg.jpg is in the drawable folder
            contentDescription = null, // Content description is null for decorative images
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.BottomCenter
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
//                .background(Color(0xFF0A0A0A))
                .padding(top = 30.dp)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Text(
                text = "Register here",
                style = TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp,
                    color = LightGreen
                )
            )

            Box(modifier = Modifier.height(40.dp))


            Image(
                painter = if (imageUri == null) painterResource(id = R.drawable.registerprofile)
                else rememberAsyncImagePainter(model = imageUri),
                contentDescription = "person",
                modifier = Modifier
                    .size(125.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable {

                        val isGranted = ContextCompat.checkSelfPermission(
                            context, permissionToRequest
                        ) == PackageManager.PERMISSION_GRANTED

                        if (isGranted) {
                            launcher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permissionToRequest)
                        }
                    },
                contentScale = ContentScale.Crop
            )

            Box(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = {
                    Text(
                        text = "  Name  ",
                        color = Color.White
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = LightGreen, // Set border color when focused
                    unfocusedBorderColor = Color.Gray, // Set border color when not focused
                    cursorColor = Color.White // Set cursor color
                )
            )
            Box(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = {
                    Text(
                        text = "   Username  ",
                        color = Color.White
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = LightGreen, // Set border color when focused
                    unfocusedBorderColor = Color.Gray, // Set border color when not focused
                    cursorColor = Color.White // Set cursor color
                )
            )
            Box(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = {
                    Text(
                        text = "  Bio  ",
                        color = Color.White
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                singleLine = false,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = LightGreen, // Set border color when focused
                    unfocusedBorderColor = Color.Gray, // Set border color when not focused
                    cursorColor = Color.White // Set cursor color
                )
            )
            Box(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(
                        text = "  Email  ",
                        color = Color.White
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = LightGreen, // Set border color when focused
                    unfocusedBorderColor = Color.Gray, // Set border color when not focused
                    cursorColor = Color.White // Set cursor color
                )
            )

            Box(modifier = Modifier.height(5.dp))


            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        text = "  Password  ",
                        color = Color.White
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = LightGreen, // Set border color when focused
                    unfocusedBorderColor = Color.Gray, // Set border color when not focused
                    cursorColor = Color.White // Set cursor color
                )
            )

            Box(modifier = Modifier.height(95.dp))



            ElevatedButton(
                onClick = {

                    if (name.isEmpty() || bio.isEmpty() || email.isEmpty() || password.isEmpty() || imageUri == null) {
                        Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        authViewModel.register(
                            email,
                            password,
                            name,
                            bio,
                            userName,
                            imageUri!!,
                            context
                        )
                    }

                }, colors = ButtonDefaults.buttonColors(Darkgreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Register",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }


            TextButton(
                onClick = {
                    navHostController.navigate(Routes.Login.routes) {
                        popUpTo(navHostController.graph.startDestinationId)
                        launchSingleTop = true
                    }

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Already register? Login here",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Darkgreen
                    )
                )
            }


        }
    }

}





@Preview(showBackground = true)
@Composable
fun RegisterView(){
 //  Register()
}