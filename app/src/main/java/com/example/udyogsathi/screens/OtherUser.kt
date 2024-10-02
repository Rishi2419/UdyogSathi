package com.example.udyogsathi.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.udyogsathi.R
import com.example.udyogsathi.item_view.ThreadItem
import com.example.udyogsathi.navigation.Routes
import com.example.udyogsathi.ui.theme.Darkgreen
import com.example.udyogsathi.ui.theme.LightGreen
import com.example.udyogsathi.ui.theme.gradientTextStyle
import com.example.udyogsathi.utils.SharedPref
import com.example.udyogsathi.viewmodel.AuthViewModel
import com.example.udyogsathi.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import coil.request.ImageRequest

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.material3.TextButton
import coil.imageLoader


@Composable
fun OtherUsers(navHostController: NavHostController, uiD: String) {
    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val context = LocalContext.current

    val userViewModel: UserViewModel = viewModel()
    val threads by userViewModel.threads.observeAsState(null)
    val users by userViewModel.users.observeAsState(null)

    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)

    userViewModel.fetchThreads(uiD)
    userViewModel.fetchUser(uiD)
    userViewModel.getFollowers(uiD)
    userViewModel.getFollowing(uiD)

    var currentUserId = ""
    if (FirebaseAuth.getInstance().currentUser != null)
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid


    var showImageDialog by remember { mutableStateOf(false) }
    var imageUrlToShow by remember { mutableStateOf("") }

    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navHostController.navigate(Routes.Login.routes) {
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    LazyColumn(modifier = Modifier.background(gradientTextStyle.brush!!)) {
        item {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 25.dp)

            ) {

                val (text, logo, userName,
                    bio, follow_button, chat_button, QR_button, followers, following, followericon, followingicon, divider) = createRefs()


                Text(
                    text = users!!.name, style = TextStyle(
                        fontSize = 32.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ), modifier = Modifier
                        .constrainAs(text) {
                            top.linkTo(parent.top, margin = 15.dp)
                            start.linkTo(parent.start)

                        }
                        .padding(start = 20.dp)
                )


                Image(painter = rememberAsyncImagePainter(model = users!!.imageUrl),
                    contentDescription = "logo",
                    modifier = Modifier
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)

                        }
                        .padding(top = 25.dp)
                        .padding(end = 20.dp)
                        .size(110.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )


                Text(
                    text = users!!.userName, style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.White
                    ), modifier = Modifier
                        .constrainAs(userName) {
                            top.linkTo(text.bottom)
                            start.linkTo(parent.start)
                        }
                        .padding(start = 20.dp)
                )


                Text(
                    text = users!!.bio, style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    ), modifier = Modifier
                        .width(250.dp)
                        .constrainAs(bio) {
                            top.linkTo(userName.bottom)
                            start.linkTo(parent.start)
                        }
                        .padding(top = 10.dp)
                        .padding(start = 20.dp)
                        .padding(bottom = 10.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.follower_icon),
                    contentDescription = "followers",
                    modifier = Modifier
                        .constrainAs(followericon) {
                            top.linkTo(bio.bottom)
                            start.linkTo(parent.start, margin = 20.dp)
                        }
                        .size(17.dp)

                )
                Text(
                    text = "${followerList?.size} Followers", style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White
                    ), modifier = Modifier
                        .constrainAs(followers) {
                            top.linkTo(followericon.top)
                            start.linkTo(followericon.start)
                        }
                        .padding(start = 20.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.follower_icon),
                    contentDescription = "following",
                    modifier = Modifier
                        .constrainAs(followingicon) {
                            top.linkTo(followericon.bottom, margin = 2.dp)
                            start.linkTo(parent.start, margin = 20.dp)
                        }
                        .size(17.dp)

                )

                Text(
                    text = "${followingList?.size} Following", style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White,
                    ), modifier = Modifier
                        .constrainAs(following) {
                            top.linkTo(followingicon.top)
                            start.linkTo(followingicon.start)
                        }
                        .padding(start = 20.dp)
                )

                ElevatedButton(onClick = {
                    if (currentUserId != "")
                        userViewModel.followUsers(uiD, currentUserId)
                   // Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show()
                },
                    modifier = Modifier
                        .constrainAs(follow_button) {
                            top.linkTo(following.bottom, margin = 10.dp)
                            start.linkTo(parent.start)

                        }
                        .padding(top = 15.dp)
                        .padding(start = 20.dp)
                        .size(width = 120.dp, height = 36.dp),
                    colors = ButtonDefaults.buttonColors(
                        Darkgreen
                    )
                )
                {
                    Text(
                        text = if (followerList != null && followerList!!.isNotEmpty() && followerList!!.contains(currentUserId))
                            "Following"
                        else
                            "Follow",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    )
                }

                ElevatedButton(onClick = {
                     Toast.makeText(context,"Chat Clicked",Toast.LENGTH_SHORT).show()
                },
                    modifier = Modifier
                        .constrainAs(chat_button) {
                            top.linkTo(follow_button.top, margin = 13.dp)
                            start.linkTo(follow_button.end, margin = 8.dp)
                            bottom.linkTo(follow_button.bottom)
                        }
                        .size(width = 110.dp, height = 36.dp),

                    colors = ButtonDefaults.buttonColors(Darkgreen)
                )
                {
                    Icon(
                        painter = painterResource(id = R.drawable.chat_icon), // Replace with your drawable resource
                        contentDescription = "Chat Icon",
                        modifier = Modifier.size(15.dp), // Adjust size as needed
                        tint = Color.White // Set the icon color
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Chat",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    )
                }

                ElevatedButton(onClick = {
                    imageUrlToShow = users!!.qrImageUrl
                    showImageDialog = true
                },
                    modifier = Modifier
                        .constrainAs(QR_button) {
                            top.linkTo(chat_button.top)
                            start.linkTo(chat_button.end, margin = 8.dp)
                            bottom.linkTo(chat_button.bottom)
                        }
                        .size(width = 106.dp, height = 36.dp),
                    colors = ButtonDefaults.buttonColors(Darkgreen)
                )
                {
                    Icon(
                        painter = painterResource(id = R.drawable.qr_code), // Replace with your drawable resource
                        contentDescription = "QR Icon",
                        modifier = Modifier
                            .size(15.dp)
                            .padding(top = 2.dp), // Adjust size as needed
                        tint = Color.White // Set the icon color
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "QR",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    )
                }
                if (showImageDialog) {
                    ShowImageDialog(
                        imageUrl = imageUrlToShow, // Pass the image URL to the dialog
                        onDismiss = { showImageDialog = false },
                        onSaveImage = {
                            saveImageToGallery(context, imageUrlToShow) // Save image function call
                        },context = context

                    )
                }

                Divider(modifier = Modifier
                    .constrainAs(divider) {
                        top.linkTo(follow_button.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(top = 20.dp),color = Color.Transparent, thickness = 0.3.dp)

            }
        }

        item {
            Box(
                modifier = Modifier
                    .height(25.dp)
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(
                            topStart = 40.dp,
                            topEnd = 40.dp
                        )
                    )
            )
        }
        if (threads != null && users != null) {
            items(threads ?: emptyList()) { pair ->
                ThreadItem(
                    thread = pair,
                    users = users!!,
                    navHostController = navHostController,
                    userId = SharedPref.getUserName(context)
                )
            }
        }


    }

}

@Composable
fun ShowImageDialog(imageUrl: String, onDismiss: () -> Unit, onSaveImage: () -> Unit,
                    context: Context) {
    Log.d("Rishi","reached here")
    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = "Enlarged Image",
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(26.dp))
                TextButton(onClick = { onSaveImage() },colors = ButtonDefaults.buttonColors(Darkgreen), modifier = Modifier.width(100.dp)) {
                    Text(
                        text = "Save", style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.White
                        )

                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}

fun saveImageToGallery(context: Context, imageUrl: String) {
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .target { drawable ->
            val bitmap = (drawable as BitmapDrawable).bitmap

            // Create a ContentValues object to insert the image into the MediaStore
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "profile_image_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES) // Save to the Pictures directory
            }

            // Insert the image into MediaStore and get the Uri
            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            uri?.let {
                // Write the bitmap to the output stream
                context.contentResolver.openOutputStream(it).use { outputStream ->
                    if (outputStream != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }
                    Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
            }
        }
        .build()

    context.imageLoader.enqueue(request)
}