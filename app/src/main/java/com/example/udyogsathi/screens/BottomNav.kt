package com.example.udyogsathi.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.udyogsathi.model.BottomNavItem
import com.example.udyogsathi.navigation.Routes
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(navHostController: NavHostController) {

    val navController1 = rememberNavController()

    Scaffold(bottomBar = {MyBottomBar(navController1)}) { innerPadding ->
        NavHost(navController = navController1, startDestination = Routes.Home.routes,
            modifier = Modifier.padding(innerPadding)){

            // Used during build time
            composable(Routes.Splash.routes){
                Splash(navController1)
            }
            composable(Routes.Home.routes){
                Home(navHostController)
            }
            composable(Routes.Notification.routes){
                Notification(navHostController)
            }
            composable(Routes.Search.routes){
                Search(navHostController)
            }
            composable(Routes.AddThread.routes){
                AddThreads(navController1)
            }
            composable(Routes.Profile.routes){
                Profile(navHostController)
            }

        }


    }
}

//@Composable
//fun MyBottomBar(navController1: NavHostController) {
//
//    val backStackEntry = navController1.currentBackStackEntryAsState()
//
//    val list = listOf(
//
//        BottomNavItem(
//            "Home",
//            Routes.Home.routes,
//            Icons.Rounded.Home
//        ),
//        BottomNavItem(
//            "Search",
//            Routes.Search.routes,
//            Icons.Rounded.Search
//        ),
//        BottomNavItem(
//            "Add Threads",
//            Routes.AddThread.routes,
//            Icons.Rounded.Add
//        ),
//        BottomNavItem(
//            "Notification",
//            Routes.Notification.routes,
//            Icons.Rounded.Notifications
//        ),
//        BottomNavItem(
//            "Profile",
//            Routes.Profile.routes,
//            Icons.Rounded.Person
//        )
//    )
//
//    BottomAppBar{
//        list.forEach {
//
//            val selected = it.route == backStackEntry?.value?.destination?.route
//
//            NavigationBarItem(selected = selected, onClick = {
//                navController1.navigate(it.route) {
//                    popUpTo(navController1.graph.findStartDestination().id) {
//                        saveState = true
//                    }
//                    launchSingleTop = true
//                }
//            }, icon = {
//                Icon(imageVector = it.icon, contentDescription = "",
//                    tint = if (selected) Color.White else Color.Gray)
//            })
//
//        }
//    }
//
//}
//
//@Composable
//fun MyBottomBar(navController1: NavHostController) {
//    BottomAppBar(
//        modifier = Modifier.background(Color.Black) // Set background color of bottom app bar
//    ) {
//        val currentRoute = navController1.currentBackStackEntryAsState().value?.destination?.route
//
//        listOf(
//            BottomNavItem(
//                "Home",
//                Routes.Home.routes,
//                Icons.Rounded.Home,
//                Color.Gray
//            ),
//            BottomNavItem(
//                "Search",
//                Routes.Search.routes,
//                Icons.Rounded.Search,
//                Color.Gray
//            ),
//            BottomNavItem(
//                "Add Threads",
//                Routes.AddThread.routes,
//                Icons.Rounded.Add,
//                Color.Gray
//            ),
//            BottomNavItem(
//                "Notification",
//                Routes.Notification.routes,
//                Icons.Rounded.Notifications,
//                Color.Gray
//            ),
//            BottomNavItem(
//                "Profile",
//                Routes.Profile.routes,
//                Icons.Rounded.Person,
//                Color.Gray
//            )
//        ).forEach { item ->
//            val isSelected = currentRoute == item.route
//
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
//                modifier = Modifier
//                    .clickable {
//                        navController1.navigate(item.route) {
//                            popUpTo(navController1.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                        }
//                    }
//                    .background(Color.Black)
//                    .fillMaxHeight()
//                    .weight(1f)
//            ) {
//                Icon(
//                    imageVector = item.icon,
//                    contentDescription = "",
//                    tint = if (isSelected) Color.White else item.iconColor // Set icon color
//                )
//
//            }
//        }
//    }
//}
//

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomBar(navController1: NavHostController) {
    BottomAppBar(
        modifier = Modifier.background(Color.Black)
            .border(width = 0.dp, color = Color.Transparent)// Set background color of bottom app bar
    ) {
        val currentRoute = navController1.currentBackStackEntryAsState().value?.destination?.route

        listOf(
            BottomNavItem(
                "Home",
                Routes.Home.routes,
                Icons.Rounded.Home,
                Color.Gray
            ),
            BottomNavItem(
                "Search",
                Routes.Search.routes,
                Icons.Rounded.Search,
                Color.Gray
            ),
            BottomNavItem(
                "Add Threads",
                Routes.AddThread.routes,
                Icons.Rounded.Add,
                Color.Gray
            ),
            BottomNavItem(
                "Notification",
                Routes.Notification.routes,
                Icons.Rounded.Notifications,
                Color.Gray
            ),
            BottomNavItem(
                "Profile",
                Routes.Profile.routes,
                Icons.Rounded.Person,
                Color.Gray
            )
        ).forEach { item ->
            val isSelected = currentRoute == item.route

            Box(
                modifier = Modifier
                    .background(Color.Black)

                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            // navigate if tapped
                            //used in runtime
                            navController1.navigate(item.route) {
                                //backstack
                                popUpTo(navController1.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()
                        .background(Color.Black)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "",
                        tint = if (isSelected) Color.White else item.iconColor // Set icon color
                    )
                }
            }
        }
    }
}
