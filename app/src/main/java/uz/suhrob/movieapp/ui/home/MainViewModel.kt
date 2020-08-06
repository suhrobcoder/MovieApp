package uz.suhrob.movieapp.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.suhrob.movieapp.data.api.Resource
import uz.suhrob.movieapp.data.db.entities.Genre
import uz.suhrob.movieapp.data.db.entities.Movie
import uz.suhrob.movieapp.data.repository.MovieRepository

class MainViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _genreDataState: MutableLiveData<Resource<List<Genre>>> = MutableLiveData()
    val genreDataState: LiveData<Resource<List<Genre>>>
        get() = _genreDataState

    fun setGenreStateEvent(genreStateEvent: GenreStateEvent) {
        viewModelScope.launch {
            when (genreStateEvent) {
                is GenreStateEvent.LoadGenres -> {
                    movieRepository.getGenres()
                        .onEach {
                            _genreDataState.value = it
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

    private val _movieDataState: MutableLiveData<Resource<List<Movie>>> = MutableLiveData()
    val movieDataState: LiveData<Resource<List<Movie>>>
        get() = _movieDataState

    fun setMovieStateEvent(movieStateEvent: MovieStateEvent) {
        viewModelScope.launch {
            when (movieStateEvent) {
                is MovieStateEvent.LoadMovies -> {
                    movieRepository.getPopular()
                        .onEach {
                            _movieDataState.value = it
                        }
                        .launchIn(viewModelScope)
                }
                is MovieStateEvent.LoadMore -> {
                    movieRepository.getPopular(loadMore = true)
                        .onEach {
                            val data = arrayListOf<Movie>()
                            if (_movieDataState.value != null && _movieDataState.value?.data != null) {
                                data.addAll(_movieDataState.value?.data!!)
                            }
                            if (it.status == Resource.Status.SUCCESS) {
                                data.addAll(it.data!!)
                            }
                            _movieDataState.value = Resource.success(data)
                        }
                        .launchIn(viewModelScope)
                }
                is MovieStateEvent.Refresh -> {
                    movieRepository.getPopular(true)
                        .onEach {
                            _movieDataState.value = it
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}

sealed class GenreStateEvent {
    object LoadGenres: GenreStateEvent()
}

sealed class MovieStateEvent {
    object LoadMovies: MovieStateEvent()
    object LoadMore: MovieStateEvent()
    object Refresh: MovieStateEvent()
}