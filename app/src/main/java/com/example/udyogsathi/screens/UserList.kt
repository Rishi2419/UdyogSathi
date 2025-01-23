package com.example.udyogsathi.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.udyogsathi.item_view.ChatUserItem
import com.example.udyogsathi.item_view.UserItem
import com.example.udyogsathi.model.UserModel
import com.example.udyogsathi.ui.theme.Gradient2
import com.example.udyogsathi.ui.theme.gradientTextStyle
import com.example.udyogsathi.viewmodel.SearchViewModel
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(navHostController: NavHostController) {


    val searchViewModel: SearchViewModel = viewModel()
    val userList by searchViewModel.usersList.observeAsState(null)

    var search by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientTextStyle.brush!!)
    ) {

        Text(
            text = "Messages", style = TextStyle(
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            ), modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 22.dp)
        )
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


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = {
                    Text(
                        text = "  Search User  "
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(start = 20.dp).padding(end = 20.dp)
                    .padding(top = 10.dp).padding(bottom = 20.dp).height(60.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.DarkGray)
                },
                textStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Gradient2 , // Set border color when focused
                    unfocusedBorderColor = Color.Gray, // Set border color when not focused
                    cursorColor = Gradient2, // Set cursor color
                    focusedLabelColor = Gradient2,
                    unfocusedLabelColor = Color.Gray
                )
            )


            LazyColumn (modifier = Modifier.padding(top = 80.dp)){


                val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
                if (userList != null && userList!!.isNotEmpty()) {
                    val filterItems = userList!!.filter {
                        it.name!!.contains(
                            search,
                            ignoreCase = true
                        ) && it.uid != currentUserUid
                    }
                    items(filterItems ?: emptyList()) { pairs ->

                        ChatUserItem(
                            senderId = currentUserUid,   // Pass the senderId
                            receiverId = pairs.uid,  // Pass the receiverId (from the `users` object)
                            receiverName = pairs.name,
                            users = pairs,
                            navHostController
                        )
                    }

                }
            }
        }
    }
}

