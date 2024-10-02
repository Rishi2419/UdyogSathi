package com.example.udyogsathi.screens

import android.content.pm.PackageManager
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.udyogsathi.R
import com.example.udyogsathi.item_view.ThreadItem
import com.example.udyogsathi.model.UserModel
import com.example.udyogsathi.navigation.Routes
import com.example.udyogsathi.ui.theme.Darkgreen
import com.example.udyogsathi.ui.theme.LightGreen
import com.example.udyogsathi.ui.theme.gradientTextStyle
import com.example.udyogsathi.ui.theme.text
import com.example.udyogsathi.utils.SharedPref
import com.example.udyogsathi.viewmodel.AuthViewModel
import com.example.udyogsathi.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Profile(navHostController: NavHostController){

    val authViewModel:AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val context = LocalContext.current

    val userViewModel:UserViewModel = viewModel()
    val threads by userViewModel.threads.observeAsState(null)

//follower and following
    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)

    var currentUserId = ""
    if (FirebaseAuth.getInstance().currentUser != null)
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

    if (currentUserId != ""){
        userViewModel.getFollowers(currentUserId)
        userViewModel.getFollowing(currentUserId)
    }


    //done
    val user = UserModel(
        name = SharedPref.getName(context),
        userName = SharedPref.getUserName(context),
        imageUrl = SharedPref.getImage(context)

    )

    if (firebaseUser != null)
        userViewModel.fetchThreads(firebaseUser!!.uid)

    LaunchedEffect(firebaseUser){
        if (firebaseUser == null){
            navHostController.navigate(Routes.Login.routes){
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop=true
            }
        }
    }
//    LaunchedEffect(Unit) {
//        if (firebaseUser == null) {
//            navHostController.navigate(Routes.Login.routes) {
//                popUpTo(navHostController.graph.startDestinationId)
//                launchSingleTop = true
//            }
//        }
//    }




    LazyColumn(modifier = Modifier.background(gradientTextStyle.brush!!)) {

        item{
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 25.dp)

            ) {

                val (title,logoutIcon,Name,logo, userName,
                    bio, followers , following,divider,followericon, followingicon)= createRefs()


                Text(text = "My Profile", style = TextStyle(
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                ), modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )

                Image(
                    painter = painterResource(id = R.drawable.logout_icon),
                    contentDescription = "Logout",
                    modifier = Modifier
                        .constrainAs(logoutIcon) {
                            top.linkTo(title.top)
                            bottom.linkTo(title.bottom)
                            end.linkTo(parent.end, margin = 16.dp)
                        }
                        .size(23.dp)
                        .clickable {
                            authViewModel.logout()
                        }
                )


                Text(text = SharedPref.getName(context), style = TextStyle(
//                    fontWeight = FontWeight.ExtraLight,
                    fontSize = 32.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ), modifier = Modifier
                    .constrainAs(Name) {
                        top.linkTo(parent.top, margin = 45.dp)
                        start.linkTo(parent.start)

                    }
                    .padding(start = 20.dp)
                )


                Image(painter = rememberAsyncImagePainter(model = SharedPref.getImage(context)),
                    contentDescription = "logo",
                    modifier = Modifier
                        .constrainAs(logo) {
                            top.linkTo(Name.top)
                            end.linkTo(parent.end)

                        }
                        .padding(top = 15.dp)
                        .padding(end = 20.dp)
                        .size(110.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )


                Text(text = SharedPref.getUserName(context), style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.White
                ), modifier = Modifier
                    .constrainAs(userName) {
                        top.linkTo(Name.bottom)
                        start.linkTo(parent.start)
                    }
                    .padding(start = 20.dp)
                )


                Text(text = SharedPref.getBio(context), style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                ), modifier = Modifier.width(250.dp)
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

                Text(text = "${followerList!!.size} Followers", style = TextStyle(
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

                Text(text = "${followingList!!.size} Following", style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.White,
                    ), modifier = Modifier
                    .constrainAs(following) {
                        top.linkTo(followingicon.top)
                        start.linkTo(followingicon.start)
                    }
                    .padding(start = 20.dp)
                )

                Divider(modifier = Modifier
                    .constrainAs(divider) {
                        top.linkTo(followingicon.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(top = 30.dp),color = Color.Transparent, thickness = 0.3.dp)

            }
        }


        // Threads list section
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

        // Render threads in the LazyColumn as a list
        items(threads ?: emptyList()) { pair ->
            ThreadItem(
                thread = pair,
                users = user,
                navHostController = navHostController,
                userId = SharedPref.getUserName(context)
            )
        }







    }
}