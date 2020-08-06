package uz.suhrob.movieapp.data.api

class RemoteDataSource(private val movieService: MovieService): BaseDataSource() {
    suspend fun getGenres() = getResult { movieService.getGenres() }

    suspend fun getMovie(movieId: Int) = getResult { movieService.getMovie(movieId) }

    suspend fun getPopular(movieId: Int) = getResult { movieService.getPopular(movieId) }

    suspend fun searchMovie(query: String, page: Int) = getResult { movieService.searchMovie(query, page) }

    suspend fun getMovieVideos(movieId: Int) = getResult { movieService.getMovieVideos(movieId) }
}