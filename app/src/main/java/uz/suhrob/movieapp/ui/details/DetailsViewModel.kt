package uz.suhrob.movieapp.ui.details

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.suhrob.movieapp.data.api.Resource
import uz.suhrob.movieapp.data.api.models.MovieInfo
import uz.suhrob.movieapp.data.api.models.VideoInfo
import uz.suhrob.movieapp.data.db.entities.Genre
import uz.suhrob.movieapp.data.db.entities.Movie
import uz.suhrob.movieapp.data.repository.MovieRepository

class DetailsViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _movie: MutableLiveData<Resource<Movie>> = MutableLiveData()
    private val _movieInfo: MutableLiveData<Resource<MovieInfo>> = MutableLiveData()
    private val _videos: MutableLiveData<Resource<List<VideoInfo>>> = MutableLiveData()

    val movie: LiveData<Resource<Movie>>
        get() = _movie
    val movieInfo: LiveData<Resource<MovieInfo>>
        get() = _movieInfo
    val videos: LiveData<Resource<List<VideoInfo>>>
        get() = _videos

    private val _genres : MutableLiveData<List<Genre>> = MutableLiveData()

    val genres: LiveData<List<Genre>>
        get() = _genres

    fun start(id: Int?) {
        if (id == null) return
        viewModelScope.launch {
            movieRepository.getMovieFromDB(id)
                .onEach {
                    _movie.value = it
                    loadGenres(it.data!!.genreIds)
                }
                .launchIn(viewModelScope)
        }
        viewModelScope.launch {
            movieRepository.getMovie(id)
                .onEach {
                    _movieInfo.value = it
                }
                .launchIn(viewModelScope)
        }
    }

    private fun loadGenres(ids: List<Int>) {
        viewModelScope.launch {
            movieRepository.getGenresById(ids)
                .onEach { _genres.value = it }
                .launchIn(viewModelScope)
        }
    }

    fun loadVideos(movieId: Int?) {
        if (movieId == null) return
        viewModelScope.launch {
            movieRepository.getMovieVideos(movieId)
                .onEach { _videos.value = it }
                .launchIn(viewModelScope)
        }
    }
}