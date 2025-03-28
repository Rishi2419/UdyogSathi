
    <!-- For Android 13 (API level 33) and above -->
    <uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />

    <!-- For older Android versions -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />


package com.example.task

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter
    private val PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewPhotos)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        photoAdapter = PhotoAdapter()
        recyclerView.adapter = photoAdapter

        findViewById<Button>(R.id.btnGetGalleryPhotos).setOnClickListener {
            requestPermissionAndLoadPhotos()
        }

        findViewById<Button>(R.id.btnGetDeletedPhotos).setOnClickListener {
            loadDeletedPhotos()
        }
    }

    private fun requestPermissionAndLoadPhotos() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            loadGalleryPhotos()
        }
    }

    private fun loadGalleryPhotos() {
        val photoList = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_ADDED + " DESC"
        )

        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (it.moveToNext()) {
                val photoPath = it.getString(columnIndex)
                photoList.add(Uri.parse("file://$photoPath"))
            }
        }

        photoAdapter.updatePhotos(photoList)
        Toast.makeText(this, "Loaded ${photoList.size} photos", Toast.LENGTH_SHORT).show()
    }

    private fun loadDeletedPhotos() {
        // Note: Accessing deleted photos requires specific Android version and manufacturer support
        // This is a simplified and may not work on all devices
        val deletedPhotoList = mutableListOf<Uri>()

        // Placeholder implementation - in real-world scenario, you'd need
        // manufacturer-specific or root-level access
        Toast.makeText(
            this,
            "Deleted photos retrieval not fully supported on all devices",
            Toast.LENGTH_LONG
        ).show()

        photoAdapter.updatePhotos(deletedPhotoList)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadGalleryPhotos()
            } else {
                Toast.makeText(
                    this,
                    "Permission denied to read storage",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}


package com.example.task

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    private val photoList = mutableListOf<Uri>()

    fun updatePhotos(newPhotos: List<Uri>) {
        photoList.clear()
        photoList.addAll(newPhotos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_item, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoUri = photoList[position]
        Glide.with(holder.itemView.context)
            .load(photoUri)
            .into(holder.imageView)
    }

    override fun getItemCount() = photoList.size

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewPhoto)
    }
}



<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    android:gravity="center">

    <Button
        android:id="@+id/btnGetGalleryPhotos"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Get Gallery Photos"
        android:layout_marginBottom="16dp"/>

    <Button
        android:id="@+id/btnGetDeletedPhotos"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Get Deleted Photos"/>

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerViewPhotos"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:layout_marginTop="16dp"/>

</LinearLayout>


<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="4dp">

    <ImageView
        android:id="@+id/imageViewPhoto"
        android:layout_width="120dp"
        android:layout_height="120dp"
        android:scaleType="centerCrop"/>

</LinearLayout>


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.task"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.task"
        minSdk = 19
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}
dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    // Glide for image loading
//    implementation("com.github.bumptech.glide:glide:4.12.0")
//    kapt("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.github.bumptech.glide:glide:4.16.0") // Updated version
    kapt("com.github.bumptech.glide:compiler:4.16.0") // Updated version

    // JUnit
    testImplementation ("junit:junit:4.13.2")

    // Android test support
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")


    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.2.1")
}


