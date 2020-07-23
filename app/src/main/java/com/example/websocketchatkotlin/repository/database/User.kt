package com.example.websocketchatkotlin.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0L,

    @ColumnInfo(name = "user_name")
    var userName: String = "",

    @ColumnInfo(name = "password")
    var password: String = ""
)