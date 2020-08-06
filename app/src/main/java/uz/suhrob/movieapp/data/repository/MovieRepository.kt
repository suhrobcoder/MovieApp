package uz.suhrob.movieapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.suhrob.movieapp.data.EntityMapper
import uz.suhrob.movieapp.data.SharedPref
import uz.suhrob.movieapp.data.api.RemoteDataSource
import uz.suhrob.movieapp.data.api.Resource
import uz.suhrob.movieapp.data.api.models.MovieInfo
import uz.suhrob.movieapp.data.api.models.VideoInfo
import uz.suhrob.movieapp.data.db.dao.GenreDao
import uz.suhrob.movieapp.data.db.dao.MovieDao
import uz.suhrob.movieapp.data.db.entities.Genre
import uz.suhrob.movieapp.data.db.entities.Movie

class MovieRepository(
    private val remoteDataSource: RemoteDataSource,
    private val movieDao: MovieDao,
    private val genreDao: GenreDao,
    private val pref: SharedPref
) {
    suspend fun getGenres(): Flow<Resource<List<Genre>>> = flow {
        emit(Resource.loading())
        if (pref.genreLoaded) {
            val genres = genreDao.getAllGenres()
            emit(Resource.success(genres))
        } else {
            val data = remoteDataSource.getGenres()
            if (data.status == Resource.Status.SUCCESS) {
                val genres = EntityMapper.genreListMapper(data.data!!.genres)
                genreDao.insert(genres)
                pref.genreLoaded = true
                emit(Resource.success(genres))
            } else {
                emit(Resource.error(data.message))
            }
        }
    }

    suspend fun getGenresById(genreIds: List<Int>): Flow<List<Genre>> = flow {
        val list: ArrayList<Genre> = ArrayList()
        for (id in genreIds) {
            list.add(genreDao.getGenreById(id))
        }
        emit(list)
    }

    suspend fun getMovie(movieId: Int): Flow<Resource<MovieInfo>> = flow {
        emit(Resource.loading())
        emit(remoteDataSource.getMovie(movieId))
    }

    suspend fun getPopular(refreshed: Boolean = false, loadMore: Boolean = false): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.loading())
        if (loadMore || pref.popularMoviesLoadedPages == 0 || refreshed) {
            val data = remoteDataSource.getPopular(pref.popularMoviesLoadedPages + 1)
            if (data.status == Resource.Status.SUCCESS) {
                if (refreshed) {
                    movieDao.clearMovies()
                    pref.popularMoviesLoadedPages = 0
                }
                val movies = EntityMapper.movieListMapper(data.data!!.results)
                pref.popularMoviesLoadedPages++
                movieDao.insertPopularMovies(movies)
            } else {
                if (!refreshed) {
                    emit(Resource.error(data.message))
                }
            }
        }
        if (pref.popularMoviesLoadedPages > 0) {
            val movies = movieDao.getAllPopularMovies()
            emit(Resource.success(movies))
        }
    }

    suspend fun getMovieFromDB(movieId: Int): Flow<Resource<Movie>> = flow {
        emit(Resource.success(movieDao.getMovieById(movieId)))
    }

    suspend fun searchMovie(query: String, page: Int = 1): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.loading())
        val data = remoteDataSource.searchMovie(query, page)
        if (data.status == Resource.Status.SUCCESS) {
            val movies = EntityMapper.movieListMapper(data.data!!.results, 0)
            movieDao.insertSearchedMovies(movies)
            emit(Resource.success(movies))
        } else {
            emit(Resource.error(data.message))
        }
    }

    suspend fun getMovieVideos(movieId: Int): Flow<Resource<List<VideoInfo>>> = flow {
        emit(Resource.loading())
        val data = remoteDataSource.getMovieVideos(movieId)
        if (data.status == Resource.Status.SUCCESS) {
            val videos = data.data!!.results
            emit(Resource.success(videos))
        } else {
            emit(Resource.error(data.message))
        }
    }
}