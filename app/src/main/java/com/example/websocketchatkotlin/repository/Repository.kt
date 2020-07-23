package com.example.websocketchatkotlin.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.websocketchatkotlin.repository.database.Message
import com.example.websocketchatkotlin.repository.database.MessageDataBase
import com.example.websocketchatkotlin.repository.database.User
import com.example.websocketchatkotlin.repository.network.Service

class Repository private constructor(application: Application) {

    private val messageDataBaseDao = MessageDataBase.getInstance(application).messagesDataBaseDao
    private val service = Service
    val messageList: LiveData<List<Message>> = getAllMessages()


    val connectionStatus = service.connectionStatus
    val newMessageReceived = service.newMessageReceived

    companion object {
        fun getInstance(application: Application): Repository {
            var instance: Repository? = null
            if (instance == null) instance = Repository(application)
            return instance
        }
    }

    init {
        initiateConnection()
    }

    //Database interface
    fun add(message: Message) {
        messageDataBaseDao.add(message)
    }

    fun delete(messageId: Long) {
        messageDataBaseDao.delete(messageId)
    }

    fun update(message: Message) {
        messageDataBaseDao.update(message)
    }

    private fun getAllMessages(): LiveData<List<Message>> = messageDataBaseDao.getAllMessages()

    fun getAllUsers(): List<User> = messageDataBaseDao.getAllUsers()

    fun addUser(user: User) = messageDataBaseDao.addUser(user)

    //Service interface
    private fun initiateConnection() {
        service.initiateSocketConnection()
    }

    fun sendNewMessage(newMessage: Message) {
        service.sendNewMessage(newMessage)
    }
}