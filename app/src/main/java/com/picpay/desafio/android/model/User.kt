package com.picpay.desafio.android.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    val img: String,
    val name: String,
    @PrimaryKey val id: Int,
    val username: String
)