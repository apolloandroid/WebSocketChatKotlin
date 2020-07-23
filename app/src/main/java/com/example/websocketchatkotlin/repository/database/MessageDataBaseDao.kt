package com.example.websocketchatkotlin.repository.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MessageDataBaseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(message: Message)

    @Query("DELETE FROM messages WHERE messages.messageId = :messageId")
    fun delete(messageId: Long)

    @Update
    fun update(message: Message)

    @Query("SELECT * FROM messages ORDER BY messageId ")
    fun getAllMessages(): LiveData<List<Message>>

//    @Query("SELECT * FROM users WHERE user_name = :userName")
//    fun getUserByUserName(userName: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User)

    @Query("SELECT * FROM users ORDER BY user_name")
    fun getAllUsers(): List<User>
}