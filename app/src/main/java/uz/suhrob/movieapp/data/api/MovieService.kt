package uz.suhrob.movieapp.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uz.suhrob.movieapp.data.api.models.GenreResponse
import uz.suhrob.movieapp.data.api.models.MovieInfo
import uz.suhrob.movieapp.data.api.models.PopularResponse
import uz.suhrob.movieapp.data.api.models.VideoResponse
import uz.suhrob.movieapp.util.Constants.Companion.API_KEY

interface MovieService {
    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") api_key: String = API_KEY
    ): Response<GenreResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = API_KEY
    ): Response<MovieInfo>

    @GET("movie/popular")
    suspend fun getPopular(
        @Query("page") page: Int = 1,
        @Query("api_key") api_key: String = API_KEY
    ): Response<PopularResponse>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("api_key") api_key: String = API_KEY
    ): Response<PopularResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = API_KEY
    ): Response<VideoResponse>
}