package uz.suhrob.movieapp.data.api

import retrofit2.Response

class RemoteDataSource(private val movieService: MovieService) {
    suspend fun getGenres() = getResult { movieService.getGenres() }

    suspend fun getMovie(movieId: Int) = getResult { movieService.getMovie(movieId) }

    suspend fun getPopular(movieId: Int) = getResult { movieService.getPopular(movieId) }

    suspend fun searchMovie(query: String, page: Int) = getResult { movieService.searchMovie(query, page) }

    suspend fun getMovieVideos(movieId: Int) = getResult { movieService.getMovieVideos(movieId) }

    private suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }
            return Resource.error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return Resource.error(e.message ?: e.toString())
        }
    }
}