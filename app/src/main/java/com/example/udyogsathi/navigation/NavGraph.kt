package com.example.udyogsathi.navigation

import android.location.Location
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
import com.example.udyogsathi.screens.OtherUsers
import com.example.udyogsathi.screens.Profile
import com.example.udyogsathi.screens.Register
import com.example.udyogsathi.screens.Search
import com.example.udyogsathi.screens.Splash
import com.example.udyogsathi.screens.Splash2
import com.example.udyogsathi.screens.UserListScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.udyogsathi.screens.ChatScreen
import com.example.udyogsathi.screens.Location

@Composable
fun NavGraph(navController: NavHostController){


    NavHost(navController = navController, startDestination = Routes.Splash.routes ){

        composable(Routes.Splash.routes){
            Splash(navController)
        }
        composable(Routes.ChatBot.routes){
            ChatBot(navController)
        }

//        composable(Routes.Chat.routes){
//            Chat(navController)
//        }

        composable(Routes.UserList.routes) {
            UserListScreen(navController)
        }
        composable(
            route = Routes.Chat.routes,
            arguments = listOf(
                navArgument("senderId") { type = NavType.StringType },
                navArgument("receiverId") { type = NavType.StringType },
                navArgument("receiverName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val senderId = backStackEntry.arguments?.getString("senderId") ?: return@composable
            val receiverId = backStackEntry.arguments?.getString("receiverId") ?: return@composable
            val receiverName = backStackEntry.arguments?.getString("receiverName") ?: "Unknown" // Retrieve receiver's name

            ChatScreen(navHostController = navController, senderId = senderId, receiverId = receiverId, receiverName = receiverName)
        }





        composable(Routes.Splash2.routes){
            Splash2(navController)
        }
        composable(Routes.Home.routes){
            Home(navController)
        }
        composable(Routes.Location.routes){
            Location(navController)
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

