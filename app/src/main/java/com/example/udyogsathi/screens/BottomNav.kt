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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import com.example.udyogsathi.ui.theme.Darkgreen
import com.example.udyogsathi.ui.theme.LightGreen
import com.example.udyogsathi.ui.theme.MediumGreen

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomBar(navController1: NavHostController) {
    BottomAppBar(
        modifier = Modifier.background(Darkgreen)
            .background(Darkgreen)
            .border(width = 0.dp, color = Color.Transparent),
        tonalElevation = 0.dp,
        containerColor = Darkgreen
    ) {
        val currentRoute = navController1.currentBackStackEntryAsState().value?.destination?.route

        listOf(
            BottomNavItem(
                "Home",
                Routes.Home.routes,
                Icons.Rounded.Home,
                Color.White
            ),
            BottomNavItem(
                "Search",
                Routes.Search.routes,
                Icons.Rounded.Search,
                Color.White
            ),
            BottomNavItem(
                "Add Threads",
                Routes.AddThread.routes,
                Icons.Rounded.Add,
                Color.White
            ),
            BottomNavItem(
                "Notification",
                Routes.Notification.routes,
                Icons.Rounded.Notifications,
                Color.White
            ),
            BottomNavItem(
                "Profile",
                Routes.Profile.routes,
                Icons.Rounded.Person,
                Color.White
            )
        ).forEach { item ->
            val isSelected = currentRoute == item.route

            Box(
                modifier = Modifier
                        //main nav bar color
                    .background(Darkgreen)

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
                Box(
                    modifier = Modifier
                        .size(45.dp) // Circle size
                        .clip(CircleShape) // Make it a circle
                        .background(if (isSelected) LightGreen else Color.Transparent), // Light green circle if selected
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "",
                        tint = if (isSelected) Darkgreen else item.iconColor // Set icon color
                    )
                }
            }
        }
    }
}
