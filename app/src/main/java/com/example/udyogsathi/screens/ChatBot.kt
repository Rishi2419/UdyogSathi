package com.example.udyogsathi.screens

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Phone

import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.udyogsathi.R
import com.example.udyogsathi.ui.theme.Darkgreen
import com.example.udyogsathi.ui.theme.UdyogSathiTheme
import com.example.udyogsathi.ui.theme.LightGreen
import com.example.udyogsathi.ui.theme.MediumGreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBot(navHostController: NavHostController) {
    // ViewModel declaration
    val chaViewModel = viewModel<ChatViewModel>()
    val chatState = chaViewModel.chatState.collectAsState().value
    val uriState = remember { MutableStateFlow("") }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            uriState.update { uri.toString() }
        }
    }

    val bitmap = getBitmap(uriState)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Darkgreen)
    ) {
//        TopAppBar(
//            title = {
//                Text(
//                    text = "ChatBot",
//                    color = Color.White,
//                    fontSize = 22.sp,
//                )
//            },
//            modifier = Modifier
//                .background(Darkgreen)
//        )
        Text(text = "ChatBot", style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = LightGreen
        ), modifier = Modifier.padding(top = 20.dp, start = 20.dp,bottom = 15.dp)
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            reverseLayout = true
        ) {
            itemsIndexed(chatState.chatList) { _, chat ->
                if (chat.isFromUser) {
                    UserChatItem(
                        prompt = chat.prompt, bitmap = chat.bitmap
                    )
                } else {
                    ModelChatItem(response = chat.prompt)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                bitmap?.let {
                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(bottom = 2.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        contentDescription = "picked image",
                        contentScale = ContentScale.Crop,
                        bitmap = it.asImageBitmap()
                    )
                }


                Icon(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(30.dp)
                        .clickable {
//                            val imagePicker = rememberLauncherForActivityResult(
//                                contract = ActivityResultContracts.PickVisualMedia()
//                            ) { uri ->
//                                uri?.let {
//                                    uriState.update { uri.toString() }
//                                }
//                            }
                            imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                    imageVector = ImageVector.vectorResource(id = R.drawable.camera),
                    contentDescription = "Add Photo"
                )
            }

            Spacer(modifier = Modifier.width(3.dp))

            TextField(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent)
                    .clip(RoundedCornerShape(30.dp))
                    .padding(4.dp),
                value = chatState.prompt,
                onValueChange = { chaViewModel.onEvent(ChatUiEvent.UpdatePrompt(it)) },
                placeholder = {
                    Text(
                        text = "Type a prompt",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                },
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MediumGreen,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(30.dp)
                    .clickable {
                        chaViewModel.onEvent(ChatUiEvent.SendPrompt(chatState.prompt, bitmap))
                        uriState.update { "" }
                    },
                imageVector = Icons.Rounded.Send,
                contentDescription = "Send prompt",
                tint = LightGreen
            )
        }
    }
}

@Composable
fun UserChatItem(prompt: String, bitmap: Bitmap?) {
    Column(
        modifier = Modifier.padding(start = 100.dp, bottom = 16.dp)
    ) {
        bitmap?.let {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(bottom = 2.dp)
                    .clip(RoundedCornerShape(30.dp)),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                bitmap = it.asImageBitmap()
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(MediumGreen)
                .padding(16.dp),
            text = prompt,
            fontSize = 17.sp,
            color = Color.White
        )
    }
}

@Composable
fun ModelChatItem(response: String) {
    Column(
        modifier = Modifier.padding(end = 100.dp, bottom = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(LightGreen)
                .padding(16.dp),
            text = response,
            fontSize = 17.sp,
            color = Darkgreen
        )
    }
}

@Composable
private fun getBitmap(uriState: MutableStateFlow<String>): Bitmap? {
    val uri = uriState.collectAsState().value

    val imageState: AsyncImagePainter.State = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri)
            .size(coil.size.Size.ORIGINAL)
            .build()
    ).state

    if (imageState is AsyncImagePainter.State.Success) {
        return imageState.result.drawable.toBitmap()
    }

    return null
    }
