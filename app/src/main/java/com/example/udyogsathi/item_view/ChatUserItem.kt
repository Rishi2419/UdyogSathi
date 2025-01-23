package com.example.udyogsathi.item_view


import android.util.Log
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.udyogsathi.R
import com.example.udyogsathi.model.UserModel
import com.example.udyogsathi.navigation.Routes
import com.example.udyogsathi.ui.theme.Darkgreen
import com.example.udyogsathi.ui.theme.Gradient1
import com.example.udyogsathi.ui.theme.Gradient2

@Composable
fun ChatUserItem(
    senderId: String,       // Add senderId as a parameter
    receiverId: String,     // Add receiverId as a parameter
    receiverName: String,
    users: UserModel,
    navHostController: NavHostController
) {

    Column(modifier = Modifier.background(Color.White)) {


        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()

                .background(Color.White)
                .clickable {
//                    // Log sender and receiver IDs for debugging
//                    Log.d("ChatNavigation", "Sender ID: $senderId")
//                    Log.d("ChatNavigation", "Receiver ID: $receiverId")
                    //navigation to new screen where two users can talk
                    navHostController.navigate("chat/$senderId/$receiverId/$receiverName")
                }
        ) {
            val (userImage, name, userName, userType) = createRefs()

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
                top.linkTo(userImage.top,margin = -5.dp)
                start.linkTo(userImage.end, margin = 15.dp)


            })

            Text(text = users.userName, style = TextStyle(
                fontSize = 14.sp, color = Color.Gray
            ), modifier = Modifier.constrainAs(userName) {
                top.linkTo(name.bottom, margin = 2.dp)
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


        }

        Divider(
            color = Color.DarkGray,
            thickness = 0.3.dp,
            modifier = Modifier.padding(top = 10.dp)
        )
    }


}

