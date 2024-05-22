package com.example.cinevote.data.repository

import com.example.cinevote.data.Film
import com.example.cinevote.data.database.Room.FilmDAO
import com.example.cinevote.data.database.Room.FilmList
import kotlinx.coroutines.flow.Flow

class FilmRepository(private val filmDAO: FilmDAO) {
    val film : Flow<List<FilmList>> = filmDAO.getAll()

    suspend fun upsert(filmList: FilmList) = filmDAO.upsert(filmList)

    suspend fun delete(filmList: FilmList) = filmDAO.delete(filmList)

    suspend fun addFilm(filmList: FilmList) = filmDAO.addFilm(filmList)

    suspend fun getFilmFromTitle(title: String) = filmDAO.getFilmFromTitle(title)
}