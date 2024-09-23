package com.example.udyogsathi.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.udyogsathi.item_view.UserItem
import com.example.udyogsathi.viewmodel.SearchViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Notification(navHostController:NavHostController){

    val searchViewModel : SearchViewModel = viewModel()
    val userList by searchViewModel.usersList.observeAsState(null)

    Column(modifier = Modifier
        .background(Color.Black)
        .fillMaxSize()) {

        Text(
            text = "Notifications", style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = Color.White
            ), modifier = Modifier.padding(top = 20.dp, start = 20.dp)
        )
        Divider(modifier = Modifier.padding(top = 20.dp),color = Color.DarkGray, thickness = 0.3.dp)

        LazyColumn{

            val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
            if (userList != null && userList!!.isNotEmpty()){
                val filterItems = userList
                items(filterItems?: emptyList()){pairs ->

                    UserItem(
                        users = pairs,
                        navHostController

                    )
                }

            }
        }
    }
}