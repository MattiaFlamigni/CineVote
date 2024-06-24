package com.example.cinevote.data.database.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDAO{

    @Query("SELECT * FROM Review")
    fun getAll(): Flow<List<Review>>
    @Upsert
    suspend fun upsert(review: Review)
    @Delete
    suspend fun delete(review: Review)
    @Insert
    suspend fun addReview(review:Review)

    /*@Query("SELECT * FROM FilmList WHERE title = :title")
    suspend fun getFilmFromTitle(title: String): FilmList

    @Query("SELECT filmID FROM FilmList Where title=:title")
    suspend fun getTitleById(title:String):Int*/

}