package com.example.websocketchatkotlin.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Message::class, User::class], version = 6, exportSchema = false)
abstract class MessageDataBase : RoomDatabase() {
    abstract val messagesDataBaseDao: MessageDataBaseDao

    companion object {
        @Volatile
        private var INSTANCE: MessageDataBase? = null

        fun getInstance(context: Context): MessageDataBase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance =
                        Room.databaseBuilder(
                            context.applicationContext, MessageDataBase::class.java,
                            "messages"
                        ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}