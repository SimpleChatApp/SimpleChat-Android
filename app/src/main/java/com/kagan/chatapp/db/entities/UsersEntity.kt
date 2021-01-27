package com.kagan.chatapp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kagan.chatapp.models.BaseVM
import java.util.*

@Entity(tableName = "users")
data class UsersEntity(
    @PrimaryKey(autoGenerate = false)
    override val Id: String,
    val UserName: String,
    val DisplayName: String
) : BaseEntity()
