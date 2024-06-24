package com.example.cinevote.data.database.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Review")
data class Review(

    @PrimaryKey val id: Int,
    @ColumnInfo val stelle : Int,
    @ColumnInfo val autore: String,
    @ColumnInfo val filmTitle: String,
    @ColumnInfo val desc: String,
)