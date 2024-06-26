package com.example.cinevote.data.database.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDAO{

    @Query("SELECT * FROM FilmList")
    fun getAll(): Flow<List<FilmList>>
    @Upsert
    suspend fun upsert(film: FilmList)
    @Delete
    suspend fun delete(film: FilmList)
    @Insert
    suspend fun addFilm(film:FilmList)

    @Query("SELECT * FROM FilmList WHERE title = :title")
    suspend fun getFilmFromTitle(title: String): FilmList

    @Query("SELECT filmID FROM FilmList Where title=:title")
    suspend fun getTitleById(title:String):Int

}