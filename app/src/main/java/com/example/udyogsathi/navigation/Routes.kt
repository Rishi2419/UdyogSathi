package com.example.udyogsathi.navigation

sealed class Routes(val routes:String){

    object Home : Routes("home")
    object Location : Routes("location")
    object Profile : Routes("profile")
    object Search : Routes("search")
    object Splash : Routes("splash")
    object AddThread : Routes("add_thread")
    object BottomNav : Routes("bottom_nav")
    object Login : Routes("login")
    object Register : Routes("register")
    object OtherUsers : Routes("register/{data}")

    object ChatBot : Routes("chatbot")

    object UserList : Routes("userList")
    object Chat : Routes("chat/{senderId}/{receiverId}/{receiverName}")


    object Splash2 : Routes("splash2")
}
