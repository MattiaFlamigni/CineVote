package com.example.cinevote.data.database.Room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FilmList::class], version = 1)
abstract class CineVoteDatabase : RoomDatabase() {
    abstract fun FilmDAO() : FilmDAO
}