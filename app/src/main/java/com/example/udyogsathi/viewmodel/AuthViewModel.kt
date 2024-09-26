package com.example.udyogsathi.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.udyogsathi.model.UserModel
import com.example.udyogsathi.navigation.Routes
import com.example.udyogsathi.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AuthViewModel: ViewModel() {

    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("users")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")
    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser : LiveData<FirebaseUser> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String, context: Context){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    getData(auth.currentUser!!.uid, context)
                    _firebaseUser.postValue(auth.currentUser)
                }
                else{
                    _error.postValue("Something went wrong.")
                }
            }
    }

    private fun getData(uid: String,context: Context) {

        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                SharedPref.storeData(userData!!.name, userData!!.email, userData!!.bio, userData!!.userName, userData!!.imageUrl,context)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun register(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri,
        context: Context
    ){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    _firebaseUser.postValue(auth.currentUser)
                    saveImage(email,password,name,bio,userName,imageUri,auth.currentUser?.uid, context )
                }
                else{
                    _error.postValue("Something went wrong.")
                }
            }
    }

    private fun saveImage(email: String, password: String, name: String, bio: String, userName: String, imageUri: Uri, uid: String?,context: Context) {

        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(email,password,name,bio,userName,it.toString(),uid,context)
            }
        }
    }

    private fun saveData(email: String, password: String, name: String, bio: String, userName: String, toString: String, uid: String?,context: Context) {


        //Followers and Following
        val firestoreDb = Firebase.firestore
        val followersRef = firestoreDb.collection("followers").document(uid!!)
        val followingRef = firestoreDb.collection("following").document(uid!!)

       // val likesRef = firestoreDb.collection("likes").document(uid!!)

        followingRef.set(mapOf("followingIds" to listOf<String>()))
        followersRef.set(mapOf("followersIds" to listOf<String>()))
        //likesRef.set(mapOf("likesIds" to listOf<String>()))


        val userData = UserModel(email,password,name,bio,userName,toString,uid!!)
        userRef.child(uid!!).setValue(userData)
            .addOnSuccessListener {
                SharedPref.storeData(name,email,bio,userName,toString,context)
                Toast.makeText(context,"Account created", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Something went wrong, try again later", Toast.LENGTH_SHORT).show()
            }

    }

      fun logout(){
        auth.signOut()
        _firebaseUser.postValue(null)
    }

}