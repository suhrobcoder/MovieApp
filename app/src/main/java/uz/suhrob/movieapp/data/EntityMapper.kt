package uz.suhrob.movieapp.data

import uz.suhrob.movieapp.data.api.models.NetworkGenre
import uz.suhrob.movieapp.data.api.models.NetworkPopularMovie
import uz.suhrob.movieapp.data.db.entities.Genre
import uz.suhrob.movieapp.data.db.entities.Movie

class EntityMapper {
    companion object {
        private fun genreMapper(networkGenre: NetworkGenre): Genre {
            return Genre(
                id = networkGenre.id,
                name = networkGenre.name
            )
        }

        fun genreListMapper(genres: List<NetworkGenre>): List<Genre> {
            return genres.map { genreMapper(it) }
        }

        private fun movieMapper(networkPopularMovie: NetworkPopularMovie, isPopular: Int = 1): Movie {
            return Movie(
                id = networkPopularMovie.id,
                backdropPath = networkPopularMovie.backdrop_path,
                posterPath = networkPopularMovie.poster_path,
                title = networkPopularMovie.title,
                overview = networkPopularMovie.overview,
                genreIds = networkPopularMovie.genre_ids,
                releaseDate = networkPopularMovie.release_date,
                video = networkPopularMovie.video,
                voteCount = networkPopularMovie.vote_count,
                voteAverage = networkPopularMovie.vote_average,
                isPopular = isPopular,
                popularity = networkPopularMovie.popularity
            )
        }

        fun movieListMapper(movies: List<NetworkPopularMovie>, isPopular: Int = 1): List<Movie> {
            return movies.map { movieMapper(it, isPopular) }
        }
    }
}