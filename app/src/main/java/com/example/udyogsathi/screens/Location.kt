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
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import com.google.firebase.auth.FirebaseAuth


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.udyogsathi.navigation.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Location(navHostController: NavHostController) {
    val context = LocalContext.current
    val mapView = MapView(context).apply {
        onCreate(Bundle())
    }

    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

    LaunchedEffect(Unit) {
        try {
            mapView.getMapAsync { googleMap ->
                
                // Reference to Firebase Realtime Database
                val databaseReference: DatabaseReference = FirebaseDatabase.getInstance()
                    .getReference("users")

                // Fetch current user location data from Firebase
                databaseReference.child(currentUserId).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val latitude = snapshot.child("latitude").getValue(String::class.java)
                        val longitude = snapshot.child("longitude").getValue(String::class.java)

                        // If latitude and longitude are available, move camera to the user's location
                        if (latitude != null && longitude != null) {
                            try {
                                // Parse latitude and longitude to LatLng
                                val userLocation = LatLng(latitude.toDouble(), longitude.toDouble())

                                // Move camera to the user's location
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 13f)) // 12f for zoom level

                                // Add a marker for the current user at their location
                                val markerIcon = BitmapDescriptorFactory.fromBitmap(
                                    getColoredMarker(context, Color.parseColor("#017E99"))
                                )
                                googleMap.addMarker(
                                    MarkerOptions()
                                        .position(userLocation)
                                        .title("You")
                                        .icon(markerIcon)
                                )
                            } catch (e: Exception) {
                                Log.e("LocationScreen", "Error parsing latitude/longitude: ${e.message}")
                            }
                        } else {
                            // Fallback location if no data found
                            val fallbackLocation = LatLng(19.9964, 73.8567) // Maharashtra
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fallbackLocation, 15f))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("LocationScreen", "Error fetching data: ${error.message}")
                    }
                })

                // Fetch other users' data and add markers as before
                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (userSnapshot in snapshot.children) {
                                val userId = userSnapshot.key
                                val name = userSnapshot.child("name").getValue(String::class.java)
                                val latitude = userSnapshot.child("latitude").getValue(String::class.java)
                                val longitude = userSnapshot.child("longitude").getValue(String::class.java)
                                val userType = userSnapshot.child("userType").getValue(String::class.java)

                                if (latitude != null && longitude != null) {
                                    try {
                                        val location = LatLng(latitude.toDouble(), longitude.toDouble())

                                        val markerIcon = if (userId == currentUserId) {
                                            BitmapDescriptorFactory.fromBitmap(
                                                getColoredMarker(context, Color.parseColor("#017E99"))
                                            )
                                        } else {
                                            when (userType) {
                                                "Wholesaler" -> BitmapDescriptorFactory.fromBitmap(
                                                    getColoredMarker(context, Color.parseColor("#784EB8"))
                                                )
                                                "Retailer" -> BitmapDescriptorFactory.fromBitmap(
                                                    getColoredMarker(context, Color.parseColor("#FF6464"))
                                                )
                                                else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                                            }
                                        }

                                        googleMap.addMarker(
                                            MarkerOptions()
                                                .position(location)
                                                .title(name ?: "Unnamed User")
                                                .snippet(userId)
                                                .icon(markerIcon)
                                        )



                                            googleMap.setOnInfoWindowClickListener { clickedMarker ->
                                                val clickedUserId = clickedMarker.snippet
                                                if (clickedUserId!= null && clickedUserId != currentUserId) {
                                                    // Show name of clicked user
                                                    Toast.makeText(context, "Clicked on: ${clickedMarker.title}", Toast.LENGTH_SHORT).show()

                                                    // Navigate to user's profile page
                                                    val routes = Routes.OtherUsers.routes.replace("{data}", clickedUserId)
                                                    navHostController.navigate(routes)
                                                }
                                            }


                                    } catch (e: Exception) {
                                        Log.e("LocationScreen", "Error parsing latitude/longitude: ${e.message}")
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("LocationScreen", "Error fetching data: ${error.message}")
                    }
                })
            }



        } catch (e: Exception) {
            Log.e("LocationScreen", "Error initializing map: ${e.message}")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mapView.onDestroy()
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        // MapView
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        // Image placed in the bottom-left corner
        Image(
            painter = painterResource(id = R.drawable.mapguide), // Replace with your mapguide image resource
            contentDescription = "Map Guide",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .height(100.dp)
                .width(170.dp)
                .padding(16.dp) // You can adjust the padding as needed
        )
    }
}

fun getColoredMarker(context: Context, color: Int, scale: Float = 2.5f): Bitmap {
    val defaultMarker = context.getDrawable(R.drawable.ic_default_marker) // Reference your vector drawable here
    val width = (defaultMarker!!.intrinsicWidth * scale).toInt()
    val height = (defaultMarker.intrinsicHeight * scale).toInt()

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    defaultMarker.setTint(color) // Apply the color tint
    defaultMarker.setBounds(0, 0, canvas.width, canvas.height)
    defaultMarker.draw(canvas)

    return bitmap
}
