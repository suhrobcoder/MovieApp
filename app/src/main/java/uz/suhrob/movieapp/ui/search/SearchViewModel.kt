package uz.suhrob.movieapp.ui.search

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.suhrob.movieapp.data.api.Resource
import uz.suhrob.movieapp.data.db.entities.Movie
import uz.suhrob.movieapp.data.repository.MovieRepository

class SearchViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _searchDataState: MutableLiveData<Resource<List<Movie>>> = MutableLiveData()
    val searchDataState: LiveData<Resource<List<Movie>>>
        get() = _searchDataState

    fun setQuery(query: String?) {
        if (query != null && query.isNotEmpty()) {
            viewModelScope.launch {
                movieRepository.searchMovie(query)
                    .onEach { _searchDataState.value = it }
                    .launchIn(viewModelScope)
            }
        }
    }
}