package com.example.udyogsathi.item_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.udyogsathi.R
import com.example.udyogsathi.model.ThreadModel
import com.example.udyogsathi.model.UserModel
import com.example.udyogsathi.ui.theme.Darkgreen
import com.example.udyogsathi.ui.theme.Gradient1
import com.example.udyogsathi.ui.theme.wholesaler


@Composable
fun ThreadItem(
    thread: ThreadModel,
    users: UserModel,
    navHostController: NavHostController,
    userId: String
) {


    Column(modifier = Modifier.background(Darkgreen)) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()

                .background(Color.White)
        ) {
            val (userImage, name, userName,userType, date, time, title, image, divider) = createRefs()

            Image(painter = rememberAsyncImagePainter(model = users.imageUrl),
                contentDescription = "userimage",
                modifier = Modifier
                    .constrainAs(userImage) {
                        top.linkTo(parent.top, margin = 20.dp)
                        start.linkTo(parent.start, margin = 20.dp)
                    }
                    .size(36.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop)


            Text(text = users.name, style = TextStyle(
                fontSize = 18.sp, color = Gradient1, fontWeight = FontWeight.Bold
            ), modifier = Modifier.constrainAs(name) {
                top.linkTo(userImage.top, margin = -5.dp)
                start.linkTo(userImage.end, margin = 15.dp)


            })

            Text(text = users.userName, style = TextStyle(
                fontSize = 14.sp, color = Color.Gray
            ), modifier = Modifier.constrainAs(userName) {
                top.linkTo(name.bottom)
                start.linkTo(userImage.end, margin = 15.dp)


            })

            if (users.userType == "Wholesaler"){
                Image(
                    painter = painterResource(id = R.drawable.wholesaler),
                    contentDescription = "wholesaler_tag",
                    modifier = Modifier
                        .constrainAs(userType) {
                            top.linkTo(name.top)
                            start.linkTo(name.end)
                            bottom.linkTo(name.bottom)
                        }
                        .size(55.dp)

                )
            }
            else{
                Image(
                    painter = painterResource(id = R.drawable.retailer),
                    contentDescription = "retailer_tag",
                    modifier = Modifier
                        .constrainAs(userType) {
                            top.linkTo(name.top)
                            start.linkTo(name.end,margin = 5.dp)
                            bottom.linkTo(name.bottom)
                        }
                        .size(45.dp)

                )


            }


            Text(text = thread.thread, style = TextStyle(
                fontSize = 16.sp, color = Color.DarkGray
            ), modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(userName.bottom, margin = 10.dp)
                    start.linkTo(userName.start)

                }
                .width(305.dp)

            )

            val timeStampParts = thread.timeStamp.split(",")
            Text(
                text = timeStampParts[0], // Display time on the first line
                style = TextStyle(fontSize = 12.sp, color = Color.Gray),
                modifier = Modifier.constrainAs(time) {
                    top.linkTo(name.top, margin = 3.dp)
                    end.linkTo(parent.end, margin = 20.dp)
                }
            )
            Text(
                text = timeStampParts.getOrNull(1) ?: "", // Display date on the second line
                style = TextStyle(fontSize = 12.sp, color = Color.Gray),
                modifier = Modifier.constrainAs(date) {
                    top.linkTo(time.bottom, margin = 1.dp)

                    end.linkTo(parent.end, margin = 20.dp)
                }
            )


            if (thread.image != "") {

                Card(modifier = Modifier
                    .constrainAs(image) {
                        top.linkTo(title.bottom, margin = 8.dp)
                        start.linkTo(title.start)

                    }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = thread.image),
                        contentDescription = "img",
                        modifier = Modifier

                            .height(400.dp)
                            .width(300.dp),
                        contentScale = ContentScale.Crop
                    )


                }
            }

            if (thread.image == "") {
                Divider(
                    color = Color.Gray,
                    thickness = 0.3.dp,
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .constrainAs(divider) {
                            top.linkTo(title.bottom, margin = 6.dp)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        }
                )
            } else {
                Divider(
                    color = Color.Gray,
                    thickness = 0.3.dp,
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .constrainAs(divider) {
                            top.linkTo(image.bottom, margin = 3.dp)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        }
                )
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun ShowThreadItem() {
    //  ThreadItem()
}