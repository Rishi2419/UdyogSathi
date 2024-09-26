package com.example.udyogsathi.item_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.udyogsathi.model.UserModel
import com.example.udyogsathi.navigation.Routes
import com.example.udyogsathi.ui.theme.Darkgreen

@Composable
fun UserItem(
    users: UserModel,
    navHostController: NavHostController
) {

    Column(modifier = Modifier.background(Darkgreen)) {


        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()

                .background(Darkgreen)
                .clickable {

                    val routes = Routes.OtherUsers.routes.replace("{data}",users.uid)
                    navHostController.navigate(routes)
                }
        ) {
            val (userImage, userName, date, time, title, image) = createRefs()

            Image(painter = rememberAsyncImagePainter(model = users.imageUrl),
                contentDescription = "userimage",
                modifier = Modifier
                    .constrainAs(userImage) {
                        top.linkTo(parent.top, margin = 20.dp)
                        start.linkTo(parent.start, margin = 20.dp)
                    }
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop)


            Text(text = users.userName, style = TextStyle(
                fontSize = 16.sp, color = Color.White
            ), modifier = Modifier.constrainAs(userName) {
                top.linkTo(userImage.top)
                start.linkTo(userImage.end, margin = 15.dp)


            })

            Text(text = users.name, style = TextStyle(
                fontSize = 16.sp, color = Color.Gray
            ), modifier = Modifier.constrainAs(title) {
                top.linkTo(userName.bottom, margin = 8.dp)
                start.linkTo(userName.start)


            })


        }

        Divider(
            color = Color.DarkGray,
            thickness = 0.3.dp,
            modifier = Modifier.padding(top = 15.dp)
        )
    }


}

