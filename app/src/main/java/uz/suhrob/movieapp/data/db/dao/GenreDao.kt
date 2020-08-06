package uz.suhrob.movieapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.suhrob.movieapp.data.db.entities.Genre

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(genres: List<Genre>)

    @Query("SELECT * FROM genre")
    suspend fun getAllGenres(): List<Genre>

    @Query("SELECT * FROM genre WHERE id = :id")
    suspend fun getGenreById(id: Int): Genre
}