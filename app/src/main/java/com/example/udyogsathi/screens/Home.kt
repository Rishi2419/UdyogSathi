package com.example.udyogsathi.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.udyogsathi.R
import com.example.udyogsathi.item_view.ThreadItem
import com.example.udyogsathi.utils.SharedPref
import com.example.udyogsathi.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.time.Duration.Companion.seconds

@Composable
fun Home(navHostController: NavHostController){

    val context = LocalContext.current
    val homeViewModel : HomeViewModel = viewModel()
    val threadsAndUsers by homeViewModel.threadsAndUsers.observeAsState(null)



    LazyColumn{

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logobnw),
                    contentDescription = null,
                    modifier = Modifier.size(45.dp)
                )
            }
        }

        items(threadsAndUsers?: emptyList()){pairs ->

            ThreadItem(
                thread = pairs.first ,
                users = pairs.second,
                navHostController,
                FirebaseAuth.getInstance().currentUser!!.uid

            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ShowHome(){
   // Home()
}
