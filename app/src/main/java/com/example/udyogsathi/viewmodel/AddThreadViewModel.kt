package com.example.udyogsathi.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.udyogsathi.model.ThreadModel
import com.example.udyogsathi.model.UserModel
import com.example.udyogsathi.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AddThreadViewModel: ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("threads")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("threads/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted : LiveData<Boolean> = _isPosted


     fun saveImage(thread: String, userId: String ,imageUri: Uri) {

        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(thread,userId,it.toString())
            }
        }
    }

     fun saveData(thread: String, userId: String ,image: String) {

         val timeStamp = SimpleDateFormat("HH.mm, dd MMM yy", Locale.getDefault()).format(Date())

         val threadData = ThreadModel(thread,image,userId,timeStamp)
        userRef.child(userRef.push().key!!).setValue(threadData)
            .addOnSuccessListener {
               _isPosted.postValue(true)

            }.addOnFailureListener {

                _isPosted.postValue(false)
            }

    }


}