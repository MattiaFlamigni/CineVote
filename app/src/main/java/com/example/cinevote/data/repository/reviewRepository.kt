package com.example.cinevote.data.repository

import com.example.cinevote.data.Film
import com.example.cinevote.data.database.Room.FilmDAO
import com.example.cinevote.data.database.Room.FilmList
import com.example.cinevote.data.database.Room.Review
import com.example.cinevote.data.database.Room.ReviewDAO
import kotlinx.coroutines.flow.Flow

class reviewRepository(private val reviewDAO: ReviewDAO) {
    val review : Flow<List<Review>> = reviewDAO.getAll()

    suspend fun upsert(review: Review) = reviewDAO.upsert(review)

    suspend fun delete(review: Review) = reviewDAO.delete(review)

    suspend fun addTReview(review: Review) = reviewDAO.addReview(review)


}