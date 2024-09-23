package com.example.udyogsathi.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.udyogsathi.item_view.ThreadItem
import com.example.udyogsathi.navigation.Routes
import com.example.udyogsathi.utils.SharedPref
import com.example.udyogsathi.viewmodel.AuthViewModel
import com.example.udyogsathi.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth


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

//    val user = UserModel(
//        name = SharedPref.getName(context),
//        userName = SharedPref.getUserName(context),
//        imageUrl = SharedPref.getImage(context)
//
//    )


    userViewModel.fetchThreads(uiD)
    userViewModel.fetchUser(uiD)
    userViewModel.getFollowers(uiD)
    userViewModel.getFollowing(uiD)

    var currentUserId = ""
    if (FirebaseAuth.getInstance().currentUser != null)
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid


    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navHostController.navigate(Routes.Login.routes) {
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    LazyColumn(modifier = Modifier.background(Color.Black)) {
        item {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp)
                    .background(Color.Black)
            ) {

                val (text, logo, userName,
                    bio, button, followers, following, divider) = createRefs()


                Text(
                    text = users!!.name, style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color.White
                    ), modifier = Modifier
                        .constrainAs(text) {
                            top.linkTo(parent.top)
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
                        .padding(top = 15.dp)
                        .padding(end = 20.dp)
                        .size(110.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )


                Text(
                    text = users!!.userName, style = TextStyle(
                        fontSize = 18.sp,
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
                        color = Color.White
                    ), modifier = Modifier
                        .constrainAs(bio) {
                            top.linkTo(userName.bottom)
                            start.linkTo(parent.start)
                        }
                        .padding(top = 10.dp)
                        .padding(start = 20.dp)
                        .padding(bottom = 10.dp)
                )

                Text(
                    text = "${followerList?.size} Followers", style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.LightGray
                    ), modifier = Modifier
                        .constrainAs(followers) {
                            top.linkTo(bio.bottom)
                            start.linkTo(parent.start)
                        }
                        .padding(start = 20.dp)
                )

                Text(
                    text = "${followingList?.size} Following", style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.LightGray,
                        fontWeight = FontWeight.ExtraLight
                    ), modifier = Modifier
                        .constrainAs(following) {
                            top.linkTo(followers.bottom)
                            start.linkTo(parent.start)
                        }
                        .padding(top = 3.dp)
                        .padding(start = 20.dp)
                )

                ElevatedButton(onClick = {
                    if (currentUserId != "")
                        userViewModel.followUsers(uiD, currentUserId)
                },
                    modifier = Modifier
                        .constrainAs(button) {
                            top.linkTo(following.bottom)
                            start.linkTo(parent.start)
                        }
                        .padding(top = 15.dp)
                        .padding(start = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        Color.White
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
                            color = Color.Black
                        )
                    )
                }

                Divider(modifier = Modifier
                    .constrainAs(divider) {
                        top.linkTo(button.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(top = 20.dp), color = Color.DarkGray, thickness = 0.3.dp)


            }
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