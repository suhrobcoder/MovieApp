package uz.suhrob.movieapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uz.suhrob.movieapp.data.db.converter.ListConverter
import uz.suhrob.movieapp.data.db.dao.GenreDao
import uz.suhrob.movieapp.data.db.dao.MovieDao
import uz.suhrob.movieapp.data.db.entities.Genre
import uz.suhrob.movieapp.data.db.entities.Movie

@Database(entities = [Genre::class, Movie::class], version = 2, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getGenreDao(): GenreDao
    abstract fun getMovieDao(): MovieDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "movie.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}