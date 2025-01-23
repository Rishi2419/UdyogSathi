package com.example.udyogsathi.screens

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import androidx.compose.material3.RadioButton
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButtonDefaults
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
import com.example.udyogsathi.ui.theme.Gradient2
import com.example.udyogsathi.ui.theme.LightGreen
import com.example.udyogsathi.ui.theme.retailer
import com.example.udyogsathi.ui.theme.wholesaler
import com.example.udyogsathi.viewmodel.AuthViewModel
import java.util.Locale

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
    var loc  by remember {
        mutableStateOf("")
    }

    var latitude by remember {
        mutableStateOf("")
    }
    var longitude by remember {
        mutableStateOf("")
    }


    var selectedOption by remember { mutableStateOf("") }
    var userType by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var qrImageUri by remember {
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

    val launcher2 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
        uri : Uri? ->
        qrImageUri = uri
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
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logincircle), // Assuming bg.jpg is in the drawable folder
            contentDescription = null, // Content description is null for decorative images
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 20.dp),
            alignment = Alignment.BottomCenter

        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // This will ensure content is spaced between the top and bottom
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Register here",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gradient2
                )

                Spacer(modifier = Modifier.height(25.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 30.dp)


                ){
                    Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                        Image(
                            painter = if (imageUri == null) painterResource(id = R.drawable.registerprofile)
                            else rememberAsyncImagePainter(model = imageUri),
                            contentDescription = "person",
                            modifier = Modifier
                                .size(125.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                                .align(Alignment.CenterVertically)
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
                        Image(
                            painter = if (qrImageUri == null) painterResource(id = R.drawable.qrregister)
                            else rememberAsyncImagePainter(model = qrImageUri),
                            contentDescription = "qrcode",
                            modifier = Modifier
                                .size(35.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                                .clickable {
                                val isGranted = ContextCompat.checkSelfPermission(
                                    context, permissionToRequest
                                ) == PackageManager.PERMISSION_GRANTED

                                if (isGranted) {
                                    launcher2.launch("image/*")
                                } else {
                                    permissionLauncher.launch(permissionToRequest)
                                }
                            },
                            contentScale = ContentScale.Crop
                        )
                    }
                }


                Spacer(modifier = Modifier.height(10.dp))


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Text(text = "Business type: ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                    modifier = Modifier.padding(start = 5.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedOption == "Wholesaler",
                            onClick = { selectedOption = "Wholesaler"
                                userType = "Wholesaler" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if (selectedOption == "Wholesaler") wholesaler else Color.Gray
                            )
                        )
                        Text(text = "Wholesaler",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (selectedOption == "Wholesaler") wholesaler else Color.Gray
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedOption == "Retailer",
                            onClick = { selectedOption = "Retailer"
                                userType = "Retailer" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if (selectedOption == "Retailer") retailer else Color.Gray
                            )
                        )
                        Text(text = "Retailer",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (selectedOption == "Retailer") retailer else Color.Gray
                        )
                    }
                }


                Spacer(modifier = Modifier.height(-10.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = " Name ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    textStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Gradient2,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Gradient2,
                        focusedLabelColor = Gradient2,
                        unfocusedLabelColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text(text = " Username ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    textStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Gradient2,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Gradient2,
                        focusedLabelColor = Gradient2,
                        unfocusedLabelColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text(text = " Bio ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = false,
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    textStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Gradient2,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Gradient2,
                        focusedLabelColor = Gradient2,
                        unfocusedLabelColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                OutlinedTextField(
                    value = loc,
                    onValueChange = { loc = it },
                    label = { Text(text = " Location ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = false,
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    textStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Gradient2,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Gradient2,
                        focusedLabelColor = Gradient2,
                        unfocusedLabelColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = " Email ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    textStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Gradient2,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Gradient2,
                        focusedLabelColor = Gradient2,
                        unfocusedLabelColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = " Password ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
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

            Spacer(modifier = Modifier.height(25.dp))

            // Buttons at the bottom
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 20.dp)
            ) {
                ElevatedButton(
                    onClick = {

                        if (name.isEmpty() || bio.isEmpty() || userType.isEmpty() || email.isEmpty() || password.isEmpty() || imageUri == null) {
                            Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT).show()
                        } else {
                            val result = convertAddressToLatLong(context, loc.toString())
                            if (result != null) {
                                latitude = result.first.toString()
                                longitude = result.second.toString()
                            } else {
                                Toast.makeText(context, "Unable to find location", Toast.LENGTH_SHORT).show()
                            }
                            authViewModel.register(
                                email,
                                password,
                                name,
                                bio,
                                userType,
                                qrImageUri!!,
                                userName,
                                imageUri!!,
                                latitude,
                                longitude,
                                context
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Darkgreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Register",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(vertical =6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(0.dp))

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

fun convertAddressToLatLong(context: Context, address: String): Pair<Double, Double>? {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocationName(address, 1)
        if (addresses != null && addresses.isNotEmpty()) {
            val location = addresses[0]
            Pair(location.latitude, location.longitude)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterView(){
 //  Register()
}