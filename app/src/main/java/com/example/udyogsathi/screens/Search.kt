package com.example.udyogsathi.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.udyogsathi.item_view.ThreadItem
import com.example.udyogsathi.item_view.UserItem
import com.example.udyogsathi.navigation.Routes
import com.example.udyogsathi.viewmodel.HomeViewModel
import com.example.udyogsathi.viewmodel.SearchViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(navHostController:NavHostController){

    val searchViewModel : SearchViewModel = viewModel()
    val userList by searchViewModel.usersList.observeAsState(null)

    var search by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.background(Color.Black).fillMaxHeight()) {

        Text(text = "Search", style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = Color.White
        ), modifier = Modifier.padding(top = 20.dp, start = 20.dp)
        )


        OutlinedTextField(
            value = search ,
            onValueChange = {search = it},
            label = { Text(text = "  Search User  ",
                color = Color.White)},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            shape = RoundedCornerShape(30.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp).padding(end = 20.dp).padding(top = 10.dp).padding(bottom = 20.dp).height(60.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.LightGray, // Set border color when focused
                unfocusedBorderColor = Color.Gray, // Set border color when not focused
                cursorColor = Color.White // Set cursor color
            )
        )


        LazyColumn{

            val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
            if (userList != null && userList!!.isNotEmpty()){
                val filterItems = userList!!.filter { it.name!!.contains(search, ignoreCase = true) && it.uid != currentUserUid}
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