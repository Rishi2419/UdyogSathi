package com.example.udyogsathi.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.udyogsathi.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places


@Composable
fun Notification(navHostController: NavHostController) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    // Initialize Places
    LaunchedEffect(Unit) {
        try {
            Places.initialize(context, context.getString(R.string.google_map_api_key))
            mapView.onCreate(null) // Initialize the MapView
            Log.d("MapView", "MapView initialized")
            mapView.getMapAsync { googleMap ->
                // Move the camera to Maharashtra, India
                val maharashtraLatLng = LatLng(19.9964, 73.8567) // Coordinates for Maharashtra
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(maharashtraLatLng, 7f))
            }
        } catch (e: Exception) {
            Log.e("MapView", "Error initializing Places: ${e.message}")
        }
    }

    // Ensure lifecycle management for the MapView
    DisposableEffect(Unit) {
        onDispose {
            // Clean up the MapView lifecycle
            mapView.onStop()
            mapView.onPause()
            mapView.onDestroy()
        }
    }

    // Render the MapView within Compose
    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize()
    )
}
