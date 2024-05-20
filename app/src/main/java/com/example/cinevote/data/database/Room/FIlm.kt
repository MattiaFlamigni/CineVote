package com.example.cinevote.data.database.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class FilmList(
    @PrimaryKey val title : String,
    @ColumnInfo val posterPath : String,
    @ColumnInfo val plot: String,
    @ColumnInfo val voteAverage: Int,
    @ColumnInfo val releaseDate: String,
    @ColumnInfo val genreIDs : String,

)