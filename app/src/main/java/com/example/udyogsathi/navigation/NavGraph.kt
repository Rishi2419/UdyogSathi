package com.example.udyogsathi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.udyogsathi.screens.AddThreads
import com.example.udyogsathi.screens.BottomNav
import com.example.udyogsathi.screens.ChatBot
import com.example.udyogsathi.screens.Home
import com.example.udyogsathi.screens.Login
import com.example.udyogsathi.screens.Notification
import com.example.udyogsathi.screens.OtherUsers
import com.example.udyogsathi.screens.Profile
import com.example.udyogsathi.screens.Register
import com.example.udyogsathi.screens.Search
import com.example.udyogsathi.screens.Splash
import com.example.udyogsathi.screens.Splash2

@Composable
fun NavGraph(navController: NavHostController){

    NavHost(navController = navController, startDestination = Routes.Splash.routes ){

        composable(Routes.Splash.routes){
            Splash(navController)
        }
        composable(Routes.ChatBot.routes){
            ChatBot(navController)
        }

        composable(Routes.Splash2.routes){
            Splash2(navController)
        }
        composable(Routes.Home.routes){
            Home(navController)
        }
        composable(Routes.Notification.routes){
            Notification(navController)
        }
        composable(Routes.Search.routes){
            Search(navController)
        }
        composable(Routes.AddThread.routes){
            AddThreads(navController)
        }
        composable(Routes.Profile.routes){
            Profile(navController)
        }
        composable(Routes.BottomNav.routes){
            BottomNav(navController)
        }

        composable(Routes.Login.routes){
            Login(navController)
        }
        composable(Routes.Register.routes){
            Register(navController)
        }
        composable(Routes.OtherUsers.routes){
            val data = it.arguments!!.getString("data")
            OtherUsers(navController,data!!)
        }
    }

}