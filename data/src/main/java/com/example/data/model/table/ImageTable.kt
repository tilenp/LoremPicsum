package com.example.data.model.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageTable (
    @PrimaryKey
    val id: String,
    val author: String,
    val downloadUrl: String,
)