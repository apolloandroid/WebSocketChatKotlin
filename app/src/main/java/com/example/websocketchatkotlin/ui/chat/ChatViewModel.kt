package com.example.websocketchatkotlin.ui.chat

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.websocketchatkotlin.repository.Repository
import com.example.websocketchatkotlin.repository.database.Message
import com.example.websocketchatkotlin.repository.database.MessageType
import com.example.websocketchatkotlin.repository.network.ConnectionStatus
import kotlinx.coroutines.*


class ChatViewModel private constructor(
    application: Application,
    private val repository: Repository
) : ViewModel() {

    val messageList: LiveData<List<Message>> = repository.messageList

    private val chatViewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + chatViewModelJob)

    private val _connectionStatus = repository.connectionStatus
    val connectionStatus: MutableLiveData<ConnectionStatus>?
        get() = _connectionStatus

    private val _newMessageReceived = repository.newMessageReceived
    val newMessageReceived: LiveData<Message>?
        get() = _newMessageReceived

    companion object {
        fun getInstance(application: Application, repository: Repository): ChatViewModel {
            var instance: ChatViewModel? = null
            if (instance == null) instance = ChatViewModel(application, repository)
            return instance
        }
    }

    fun sendNewMessage(messageText: String, messageSender: String) {
        val newMessage = createNewMessage(messageText, messageSender)
        uiScope.launch {
            addNewMessageToDatabase(newMessage)
            sendNewMessage(newMessage)
        }
    }

    private suspend fun sendNewMessage(newMessage: Message) {
        withContext(Dispatchers.IO) {
            repository.sendNewMessage(newMessage)
        }
    }

    fun receiveNewMessage() {
        uiScope.launch {
            if (newMessageReceived != null) {
                newMessageReceived!!.value?.let { addNewMessageToDatabase(it) }
            }
        }
    }

    private fun createNewMessage(
        messageText: String,
        messageSender: String
    ): Message {
        val newMessage = Message()
        newMessage.messageText = messageText
        newMessage.messageSender = messageSender
        newMessage.messageType = MessageType.SENT.toString()
        return newMessage
    }

    private suspend fun addNewMessageToDatabase(newMessage: Message) {
        withContext(Dispatchers.IO) {
            repository.add(newMessage)
        }
    }
}
