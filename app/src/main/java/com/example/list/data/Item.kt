package com.example.list.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val itemName: String,
    val quantity: String,
    val category: String,
)