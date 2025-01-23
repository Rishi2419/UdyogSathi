package com.example.udyogsathi.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.socket.client.Socket
import org.json.JSONObject
import android.util.Base64
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.udyogsathi.R
import java.io.ByteArrayOutputStream
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import java.io.InputStream


import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.udyogsathi.ui.theme.gradientTextStyle
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Send
import com.example.udyogsathi.ui.theme.Darkgreen
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navHostController: NavHostController,
    senderId: String,
    receiverId: String,
    receiverName: String
) {
    var socket by remember { mutableStateOf<Socket?>(null) }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    var message by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current // Get context here


    // Firestore reference
    val db = FirebaseFirestore.getInstance()
    val chatRoomId = if (senderId < receiverId) "$senderId-$receiverId" else "$receiverId-$senderId"

    LaunchedEffect(Unit) {

        db.collection("chats")
            .document(chatRoomId)
            .collection("messages")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.documents.forEach { doc ->
                    val text = doc.getString("text")
                    val imageBase64 = doc.getString("imageBase64")
                    val isImage = doc.getBoolean("isImage") ?: false
                    //val isFromSender = doc.getBoolean("isFromSender") ?: false
                    val messageSenderId = doc.getString("senderId") ?: ""
                    val isFromSender = messageSenderId == senderId


                    val bitmap = if (isImage) decodeBase64ToImage(imageBase64 ?: "") else null
                    messages.add(ChatMessage(text, bitmap, isImage, imageBase64, isFromSender))
                }
            }
        SocketHandler.initSocket()
        socket = SocketHandler.socket
        SocketHandler.connectSocket()

        socket?.emit("joinRoom", JSONObject(mapOf("senderId" to senderId, "receiverId" to receiverId)))
        Log.d("CHATSCREEN", "Joined room: $senderId and $receiverId")
    }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
        uri?.let {
            sendImageMessage(socket, senderId, receiverId, it, context,db,chatRoomId) // Send image to the server
            // Add image to the sender's message list immediately
            val imageBase64 = encodeImageToBase64(context, it)
            val bitmap = decodeBase64ToImage(imageBase64)
            messages.add(ChatMessage(image = bitmap, isImage = true, imageBase64 = imageBase64, isFromSender = true))
        }
    }

    LaunchedEffect(socket) {
        socket?.on("receiveMessage") { args ->
            val data = args[0] as JSONObject
            val senderIdFromServer = data.getString("senderId")

            if (data.has("message") && senderIdFromServer != senderId) {
                val receivedMessage = data.getString("message")
                if (!messages.any { it.text == receivedMessage && !it.isImage }) {
                    Log.d("CHATSCREEN", "Received message: $receivedMessage")
                    messages.add(ChatMessage(receivedMessage, isImage = false))
                }
            }

            if (data.has("image") && senderIdFromServer != senderId) {
                val imageBase64 = data.getString("image")
                // Prevent adding duplicate received images by comparing the Base64 string
                if (!messages.any { it.isImage && it.imageBase64 == imageBase64 }) {
                    val bitmap = decodeBase64ToImage(imageBase64)
                    messages.add(ChatMessage(image = bitmap, isImage = true, imageBase64 = imageBase64))
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientTextStyle.brush!!)
    ) {

        Text(
            text = receiverName, style = TextStyle(
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            ), modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 22.dp)
        )

//        Icon(
//            imageVector = Icons.Default.Call,
//            contentDescription = "Calling",
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .size(45.dp)
//                .padding(top = 22.dp, end = 20.dp)
//                .clickable {
//
//                }
//        )


    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 65.dp)
            .background(gradientTextStyle.brush!!)
            .clip(
                RoundedCornerShape(
                    topStart = 25.dp,
                    topEnd = 25.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
    )
    {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(bottom = 3.dp,
                top = 16.dp,
                start = 16.dp,
                end = 16.dp)
    ) {
        // Display messages in a scrollable column
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)  // Make this column scrollable
        ) {
            messages.forEach { msg ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = if (msg.isFromSender) {
                        Arrangement.End // Align items to the end if it's from the sender
                    } else {
                        Arrangement.Start // Align items to the start if it's from the receiver
                    }
                ) {
                if (msg.isImage) {
                    msg.image?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Received Image",
                            modifier = Modifier
//
                                .padding(8.dp)
                                .size(height = 400.dp, width = 180.dp)

                        )
                    }
                } else {
                    //Text(text = msg.text ?: "", color = Color.Gray)

                    //NEW
                    Text(
                        text = msg.text ?: "",
                        color = Color.White,
                        modifier = Modifier
                            //.align(if (msg.isFromSender) Alignment.End else Alignment.Start)
                            .clip(RoundedCornerShape(
                                topStart = if (msg.isFromSender) 15.dp else 0.dp,
                                topEnd = if (msg.isFromSender) 0.dp else 15.dp,
                                bottomStart = 15.dp,
                                bottomEnd = 15.dp))
                            .background(if (msg.isFromSender) Color(0xFF01BE84) else Color(0xFF007B9A))
                            .padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }}

        // Input and send button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp)
        ) {
            // NEW
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.camera),
                contentDescription = "Pick Image",
                modifier = Modifier
                    .size(36.dp)
                    .padding(end = 8.dp)
                    .clickable { imagePickerLauncher.launch("image/*") }
            )

            TextField(
                value = message,
                onValueChange = { message = it },
                placeholder = { Text(
                    text = "Type a message",
                    color = Color.White,
                    fontSize = 16.sp
                )},
                textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
                    .background(Color.Transparent)
                    .clip(RoundedCornerShape(30.dp))
                    .padding(1.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.LightGray,
                    cursorColor = Darkgreen,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send Message",
                modifier = Modifier
                    .size(36.dp)
                    .padding(start = 8.dp)
                    .clickable {
                        sendMessage(socket, senderId, receiverId, message,db,chatRoomId)
                        messages.add(ChatMessage("You: $message", isImage = false, isFromSender = true))
                        message = ""
                    }
            )


        }
    }

        }

}

// Data class to handle text and image messages
data class ChatMessage(val text: String? = null, val image: Bitmap? = null, val isImage: Boolean, val imageBase64: String? = null,val isFromSender: Boolean = false)

// Send text message
private fun sendMessage(socket: Socket?, senderId: String, receiverId: String, message: String,db: FirebaseFirestore,
                        chatRoomId: String) {
    val timestamp = System.currentTimeMillis()
    val data = JSONObject().apply {
        put("senderId", senderId)
        put("receiverId", receiverId)
        put("message", message)
    }
    socket?.emit("sendMessage", data)

    val messageData = hashMapOf(
        "senderId" to senderId,
        "receiverId" to receiverId,
        "text" to message,
        "imageBase64" to null,
        "timestamp" to timestamp,
        "isImage" to false,
        "isFromSender" to true
    )

    db.collection("chats")
        .document(chatRoomId)
        .collection("messages")
        .add(messageData)
}

// Send image message
private fun sendImageMessage(
    socket: Socket?,
    senderId: String,
    receiverId: String,
    imageUri: Uri,
    context: Context, // Add context parameter here
    db: FirebaseFirestore,
    chatRoomId: String
) {
    val imageData = encodeImageToBase64(context, imageUri)
    val timestamp = System.currentTimeMillis()
    val data = JSONObject().apply {
        put("senderId", senderId)
        put("receiverId", receiverId)
        put("image", imageData)
    }
    socket?.emit("sendMessage", data)

    // Add to Firestore
    val messageData = hashMapOf(
        "senderId" to senderId,
        "receiverId" to receiverId,
        "text" to null,
        "imageBase64" to imageData,
        "timestamp" to timestamp,
        "isImage" to true,
        "isFromSender" to true
    )

    db.collection("chats")
        .document(chatRoomId)
        .collection("messages")
        .add(messageData)
}

// Encode image to Base64
private fun encodeImageToBase64(context: Context, imageUri: Uri): String {
    val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
}

// Decode Base64 to Bitmap
private fun decodeBase64ToImage(base64Str: String): Bitmap {
    val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

