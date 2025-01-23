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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import com.example.udyogsathi.ui.theme.Darkgreen
import com.example.udyogsathi.ui.theme.Gradient1
import com.example.udyogsathi.ui.theme.Gradient2
import com.example.udyogsathi.ui.theme.Gradient3
import com.example.udyogsathi.ui.theme.gradientTextStyle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(navHostController: NavHostController) {

    val navController1 = rememberNavController()

    Scaffold(bottomBar = {MyBottomBar(navController1)}) { innerPadding ->
        NavHost(navController = navController1, startDestination = Routes.Home.routes,
            modifier = Modifier.padding(innerPadding)
                .background(Color.White)){

            // Used during build time
            composable(Routes.Splash.routes){
                Splash(navController1)
            }
            composable(Routes.Home.routes){
                Home(navHostController)
            }
            composable(Routes.Location.routes){
                Location(navHostController)
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.White) // Apply gradient to the entire BottomAppBar
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip( RoundedCornerShape(
                    topStart = 25.dp,
                    topEnd = 25.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ))
                .background(gradientTextStyle.brush!!) // Apply gradient to the entire BottomAppBar
        ) {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 0.dp,
                containerColor = Color.Transparent // Set the containerColor to transparent
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
                        "Location",
                        Routes.Location.routes,
                        Icons.Rounded.LocationOn,
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
                            .pointerInput(Unit) {
                                detectTapGestures { offset ->
                                    // Navigate if tapped
                                    navController1.navigate(item.route) {
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
                        // This box creates the circular icon background
                        Box(
                            modifier = Modifier
                                .size(42.dp) // Circle size
                                .clip(RoundedCornerShape(22.dp)) // Make it a circle
                                .background(if (isSelected) Color.White.copy(alpha = 0.3f) else Color.Transparent) // Transparent background
                                .blur(8.dp), // Apply a blur for the glass effect
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = "",
                                tint = if (isSelected) item.iconColor else item.iconColor // Set icon color
                            )
                        }
                    }
                }
            }
        }
    }

}
