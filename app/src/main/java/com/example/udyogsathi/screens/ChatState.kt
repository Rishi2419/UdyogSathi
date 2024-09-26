package com.example.udyogsathi.screens

import android.graphics.Bitmap
import com.example.udyogsathi.data.Chat


data class ChatState (
    val chatList: MutableList<Chat> = mutableListOf(),
    val prompt: String = "",
    val bitmap: Bitmap? = null
)