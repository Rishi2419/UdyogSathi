package com.example.udyogsathi.screens

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {
    lateinit var socket: Socket

    @Synchronized
    fun initSocket() {
        try {
            //socket = IO.socket("http://192.168.204.44:5000")
            socket = IO.socket("http://192.168.175.44:5000")
            socket.connect()
            socket.on(Socket.EVENT_CONNECT) {
                Log.d("CHATSCREEN", "Socket connected") // This should show when the connection is successful
            }

            socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
                // 'args' is a list of objects, so we need to cast it to get the error message
                if (args.isNotEmpty()) {
                    Log.d("CHATSCREEN", "Socket connection error: ${args[0]}")
                }
            }

            socket.on(Socket.EVENT_DISCONNECT) {
                Log.d("CHATSCREEN", "Socket connection timeout")
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun connectSocket() {
        if (!socket.connected()) {
            socket.connect()
        }
    }

    @Synchronized
    fun disconnectSocket() {
        if (socket.connected()) {
            socket.disconnect()
        }
    }
}
