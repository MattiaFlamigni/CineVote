package com.example.cinevote.data.database.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [FilmList::class, Review::class], version = 3)
abstract class CineVoteDatabase : RoomDatabase() {
    abstract fun FilmDAO() : FilmDAO
    abstract fun ReviewDAO() : ReviewDAO
}


val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Esegui le operazioni necessarie per aggiornare il database
        db.execSQL("ALTER TABLE users ADD COLUMN age INTEGER DEFAULT 0 NOT NULL")
    }
}
