package com.example.mobilecwtwo.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovies(vararg user: Movie)

    @Query("SELECT * FROM Movie WHERE Actors LIKE '%'||:name||'%'  ")
    fun findMovieByActor(name: String): List<Movie>
}