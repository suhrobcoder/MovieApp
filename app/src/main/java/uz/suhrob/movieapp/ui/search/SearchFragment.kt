package uz.suhrob.movieapp.ui.search

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.Hold
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.suhrob.movieapp.R
import uz.suhrob.movieapp.data.api.Resource
import uz.suhrob.movieapp.extension.setupStatusBar
import uz.suhrob.movieapp.extension.setupToolbar


@AndroidEntryPoint
class SearchFragment: Fragment() {
    private val viewModel: SearchViewModel by viewModels()
    private var query = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        setupViews()
        setHasOptionsMenu(true)
        setupStatusBar(false)
        setupToolbar(search_toolbar)
        exitTransition = Hold()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.queryHint = "Search Movie"
        searchView.post {
            searchView.setQuery(query, false)
        }
        var job: Job? = null
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (query == newText || newText?.isEmpty() == true) return false
                query = newText ?: ""
                job?.cancel()
                job = MainScope().launch {
                    delay(500)
                    viewModel.setQuery(newText)
                }
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    viewModel.setQuery(query)
                }
                return false
            }
        })
        searchItem.expandActionView()
        searchItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                findNavController().popBackStack()
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupViews() {
        search_recyclerview.layoutManager = LinearLayoutManager(context)
        val searchAdapter = SearchAdapter()
        searchAdapter.onItemClick = { movie, view ->
            val movieTransitionName = getString(R.string.movie_details_transition)
            val extras = FragmentNavigatorExtras(view to movieTransitionName)
            val directions = SearchFragmentDirections.actionSearchFragmentToDetailsFragment2(movie.id)
            findNavController().navigate(
                directions,
                extras
            )
        }

        search_recyclerview.adapter = searchAdapter
        viewModel.searchDataState.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                Resource.Status.SUCCESS -> {
                    searchAdapter.submitList(it.data)
                    search_recyclerview.visibility = View.VISIBLE
                    search_progressbar.visibility = View.GONE
                }
                Resource.Status.LOADING -> {
                    search_progressbar.visibility = View.VISIBLE
                    search_recyclerview.visibility = View.GONE

                }
                Resource.Status.ERROR -> {
                    Toast.makeText(context, "Yuklashda xatolik", Toast.LENGTH_SHORT).show()
                    search_progressbar.visibility = View.GONE
                    search_recyclerview.visibility = View.GONE
                }
            }
        })
    }
}