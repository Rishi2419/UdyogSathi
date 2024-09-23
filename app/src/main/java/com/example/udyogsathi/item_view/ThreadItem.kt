package com.example.udyogsathi.item_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.udyogsathi.R
import com.example.udyogsathi.model.ThreadModel
import com.example.udyogsathi.model.UserModel

@Composable
fun ThreadItem(
    thread: ThreadModel,
    users: UserModel,
    navHostController: NavHostController,
    userId: String
) {


        Column(modifier = Modifier.background(Color.Black)) {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()

                    .background(Color.Black)
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

                Text(text = thread.thread, style = TextStyle(
                    fontSize = 16.sp, color = Color.White
                ), modifier = Modifier.constrainAs(title) {
                    top.linkTo(userName.bottom, margin = 15.dp)
                    start.linkTo(userName.start)



                })

//                Text(text = thread.timeStamp, style = TextStyle(
//                    fontSize = 14.sp, color = Color.DarkGray
//                ), modifier = Modifier.constrainAs(time) {
//                    top.linkTo(userName.top)
//                    bottom.linkTo(userName.bottom)
//                    end.linkTo(parent.end, margin = 20.dp)
//
//                })

                val timeStampParts = thread.timeStamp.split(",")
                Text(
                    text = timeStampParts[0], // Display time on the first line
                    style = TextStyle(fontSize = 12.sp, color = Color.DarkGray),
                    modifier = Modifier.constrainAs(time) {
                        top.linkTo(userName.top)

                        end.linkTo(parent.end, margin = 20.dp)
                    }
                )
                Text(
                    text = timeStampParts.getOrNull(1) ?: "", // Display date on the second line
                    style = TextStyle(fontSize = 12.sp, color = Color.DarkGray),
                    modifier = Modifier.constrainAs(date) {
                        top.linkTo(time.bottom, margin = 1.dp)

                        end.linkTo(parent.end,margin = 20.dp)
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


            }

            Divider(
                color = Color.DarkGray,
                thickness = 0.3.dp,
                modifier = Modifier.padding(top = 15.dp)
            )
        }
    }





@Preview(showBackground = true)
@Composable
fun ShowThreadItem() {
    //  ThreadItem()
}