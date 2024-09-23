package com.example.udyogsathi.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title : String,
    val route : String,
    val icon : ImageVector,
    val iconColor: Color
)
