package uz.suhrob.movieapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String?,
    @ColumnInfo(name = "poster_path") val posterPath: String?,
    @ColumnInfo(name = "genre_ids") val genreIds: List<Int>,
    @ColumnInfo(name = "release_date") val releaseDate: String,
    @ColumnInfo(name = "video") val video: Boolean,
    @ColumnInfo(name = "popularity") val popularity : Double,
    @ColumnInfo(name = "vote_count") val voteCount: Int,
    @ColumnInfo(name = "vote_average") val voteAverage: Double,
    @ColumnInfo(name = "is_popular") val isPopular: Int
) {
    fun getMovieReleaseYear(): String {
        return releaseDate.split("-")[0]
    }
}