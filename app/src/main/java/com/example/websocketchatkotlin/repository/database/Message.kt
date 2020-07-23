package com.example.websocketchatkotlin.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true)
    var messageId: Long = 0L,
    @ColumnInfo(name = "message_text")
    var messageText: String = "",
    @ColumnInfo(name = "message_sender")
    var messageSender: String = "",
    @ColumnInfo(name = "message_time")
    val messageTime: Long = Date().time,
    @ColumnInfo(name = "message_type")
    var messageType: String = ""
)

enum class MessageType {
    SENT, RECEIVED
}