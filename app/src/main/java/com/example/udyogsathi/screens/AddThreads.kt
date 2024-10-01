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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.udyogsathi.R
import com.example.udyogsathi.navigation.Routes
import com.example.udyogsathi.ui.theme.Darkgreen
import com.example.udyogsathi.ui.theme.Gradient1
import com.example.udyogsathi.ui.theme.Gradient2
import com.example.udyogsathi.ui.theme.LightGreen
import com.example.udyogsathi.ui.theme.gradientTextStyle
import com.example.udyogsathi.ui.theme.text
import com.example.udyogsathi.utils.SharedPref
import com.example.udyogsathi.viewmodel.AddThreadViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddThreads(navHostController: NavHostController) {

    val threadViewModel: AddThreadViewModel = viewModel()
    val isPosted by threadViewModel.isPosted.observeAsState(false)

    val context = LocalContext.current

    var thread by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else android.Manifest.permission.READ_EXTERNAL_STORAGE


    var isPosting by remember { mutableStateOf(false) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {

                isGranted: Boolean ->
            if (isGranted) {

            } else {

            }
        }


    LaunchedEffect(isPosted) {
        if (isPosted!!) {
            thread = ""
            imageUri = null
            Toast.makeText(context, "Post added", Toast.LENGTH_SHORT).show()

            navHostController.navigate(Routes.Home.routes) {
                popUpTo(Routes.AddThread.routes) {
                    inclusive = true
                }
            }
        }
    }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientTextStyle.brush!!)
        ) {

//
//        val (crossPic, text, divider,logo, userName, editText,
//            attachMedia, replyText, button, imageBox) = createRefs()//


            Image(painter = painterResource(id = R.drawable.clear),
                contentDescription = "close",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 20.dp)
                    .padding(top = 25.dp)
                    .clickable {

                        navHostController.navigate(Routes.Home.routes) {
                            popUpTo(Routes.AddThread.routes) {
                                inclusive = true
                            }
                        }

                    })

            Text(
                text = "New Post", style = TextStyle(
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                ), modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 22.dp)
            )


//        Divider( color = Color.DarkGray, thickness = 0.3.dp, modifier = Modifier.padding(top=15.dp))
//        Divider(modifier = Modifier.constrainAs(divider){
//            top.linkTo(crossPic.top)
//            start.linkTo(parent.start)
//            end.linkTo(parent.end)
//        }
//            .padding(top = 60.dp),color = Color.DarkGray, thickness = 0.3.dp)
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

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
//
                val (logo, Name , userName, editText, attachMedia, replyText, button, imageBox) = createRefs()//

                Image(painter = rememberAsyncImagePainter(model = SharedPref.getImage(context)),
                    contentDescription = "logo",
                    modifier = Modifier
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                        .padding(top = 25.dp)
                        .padding(start = 20.dp)
                        .size(36.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )


                Text(text = SharedPref.getName(context), style = TextStyle(
                    fontSize = 18.sp,
                    color = Gradient1, fontWeight = FontWeight.Bold
                ), modifier = Modifier.constrainAs(Name) {
                    top.linkTo(logo.top)
                    start.linkTo(logo.end, margin = 15.dp)
                    bottom.linkTo(logo.bottom)
                }
                )

                Text(text = SharedPref.getUserName(context), style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray
                ), modifier = Modifier.constrainAs(userName) {
                    top.linkTo(Name.bottom)
                    start.linkTo(logo.end, margin = 15.dp)
                    bottom.linkTo(logo.bottom)
                }
                )


                BasicTextFieldWithHint(
                    hint = "Start a post...",
                    value = thread,
                    onValueChange = { thread = it },
                    modifier = Modifier
                        .constrainAs(editText) {
                            top.linkTo(
                                userName.bottom,
                                margin = 14.dp
                            ) // Adjust top constraint to be below userName
                            start.linkTo(userName.start) // Align with the start of userName
                            end.linkTo(parent.end, margin = 5.dp) // Align with the end of the parent ConstraintLayout
                        }
                        .fillMaxWidth()
                )

                if (imageUri == null) {
                    Image(painter = painterResource(id = R.drawable.attach),
                        contentDescription = "close",
                        modifier = Modifier
                            .constrainAs(attachMedia) {
                                top.linkTo(editText.bottom)
                                start.linkTo(editText.start,margin =5.dp)
                            }
                            .size(43.dp)
                            .padding(start = 25.dp)
                            .padding(top = 10.dp)
                            .clickable {

                                val isGranted = ContextCompat.checkSelfPermission(
                                    context, permissionToRequest
                                ) == PackageManager.PERMISSION_GRANTED

                                if (isGranted) {
                                    launcher.launch("image/*")
                                } else {
                                    permissionLauncher.launch(permissionToRequest)
                                }
                            })
                } else {
                    Box(modifier = Modifier
                        .background(Darkgreen)
                        .padding(12.dp)
                        .constrainAs(imageBox) {
                            top.linkTo(editText.bottom)
                            start.linkTo(editText.start)
                            end.linkTo(parent.end)
                        }
                        .height(400.dp)
                        .width(300.dp)) {


                        Image(
                            painter = rememberAsyncImagePainter(model = imageUri),
                            contentDescription = "img",
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentScale = ContentScale.Crop
                        )
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Remove Image",
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .clickable {
                                    imageUri = null
                                })

                    }
                }



                if (isPosting) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(30.dp)
                            .constrainAs(button) {
                                // Place the CircularProgressIndicator in the same position as the button
                                end.linkTo(parent.end, margin = 20.dp)
                                bottom.linkTo(parent.bottom, margin = 12.dp)
                            }, color = Darkgreen
                    )
                } else {
                    TextButton(onClick = {


                        if (thread.isEmpty() && imageUri == null) {
                            Toast.makeText(
                                context,
                                "No post or media attached",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            isPosting = true
                            if (imageUri == null) {
                                threadViewModel.saveData(
                                    thread,
                                    FirebaseAuth.getInstance().currentUser!!.uid,
                                    ""
                                )
                            } else {
                                threadViewModel.saveImage(
                                    thread,
                                    FirebaseAuth.getInstance().currentUser!!.uid,
                                    imageUri!!
                                )
                            }
                        }


                    }, modifier = Modifier.constrainAs(button) {
                        end.linkTo(parent.end, margin = 20.dp)
                        bottom.linkTo(parent.bottom, margin = 12.dp)
                    },
                        colors = ButtonDefaults.buttonColors(Darkgreen)
                    ) {

                        Text(
                            text = "Post", style = TextStyle(
                                fontSize = 18.sp,
                                color = Color.White
                            )

                        )
                    }


            }


        }
    }
}


@Composable
fun BasicTextFieldWithHint(
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {

    Box(modifier = modifier) {
        if (value.isEmpty()) {
            Text(
                text = hint,
                color = Color.Gray,
                fontSize = 16.sp,

                modifier = Modifier
                    .padding(horizontal = 33.dp)
                    .padding(top = 5.dp)
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle.Default.copy(color = Color.DarkGray, fontSize = 18.sp, FontWeight.Light, letterSpacing = 0.5.sp),

            modifier = Modifier
                .padding(horizontal = 33.dp)
                .padding(top = 5.dp)
                .fillMaxWidth()
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddPostView() {
    //AddThreads()
}