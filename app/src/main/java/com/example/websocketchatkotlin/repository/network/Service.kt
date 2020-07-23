package com.example.websocketchatkotlin.repository.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.websocketchatkotlin.repository.database.Message
import com.example.websocketchatkotlin.repository.database.MessageType
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*


object Service {

    private val SERVER_PATH: String = "ws://192.168.42.48:3000"
    private lateinit var webSocket: WebSocket

    private val gson = Gson()

    var connectionStatus = MutableLiveData<ConnectionStatus>()
    private val _newMessageReceived: MutableLiveData<Message> = MutableLiveData()
    val newMessageReceived: LiveData<Message>
        get() = _newMessageReceived

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    fun initiateSocketConnection() {
        val chatClient = OkHttpClient()
        val request = Request.Builder().url(SERVER_PATH).build()
        webSocket = chatClient.newWebSocket(request, SocketListener)
    }

    private suspend fun setConnectionStatusOk() {
        withContext(Dispatchers.IO) {
            connectionStatus.postValue(ConnectionStatus.OK)
        }
    }

    private suspend fun setConnectionStatusFailed() {
        withContext(Dispatchers.IO) {
            connectionStatus.postValue(ConnectionStatus.FAILED)
        }
    }

    fun sendNewMessage(newMessage: Message) {
        val newMessageJsonString: String = gson.toJson(newMessage)
        serviceScope.launch {
            sendNewMessage(newMessageJsonString)
        }
    }

    private suspend fun sendNewMessage(newMessage: String) {
        withContext(Dispatchers.IO) {
            webSocket.send(newMessage)
        }
    }

    private suspend fun receiveNewMessage(newMessage: Message) {
        withContext(Dispatchers.IO) {
            _newMessageReceived.postValue(newMessage)
        }
    }

    private object SocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket?, response: Response?) {
            super.onOpen(webSocket, response)
            serviceScope.launch {
                setConnectionStatusOk()
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            val receivedJsonMessage = gson.fromJson(text, Message::class.java)
            receivedJsonMessage.messageType = MessageType.RECEIVED.toString()
            serviceScope.launch {
                receiveNewMessage(receivedJsonMessage)
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            serviceScope.launch {
                setConnectionStatusFailed()
            }
        }
    }
}