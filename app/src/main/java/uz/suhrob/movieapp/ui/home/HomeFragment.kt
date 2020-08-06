package uz.suhrob.movieapp.ui.home

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import uz.suhrob.movieapp.R
import uz.suhrob.movieapp.data.api.Resource
import uz.suhrob.movieapp.data.db.entities.Genre
import uz.suhrob.movieapp.extension.setupStatusBar
import uz.suhrob.movieapp.extension.setupToolbar
import uz.suhrob.movieapp.ui.BaseFragment
import uz.suhrob.movieapp.util.MetricUtils

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private lateinit var movieAdapter: MovieAdapter
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_home)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasInitializedRootView) {
            setupViews()
            setupObservers()
            setHasOptionsMenu(true)
            hasInitializedRootView = true
        }
        setupStatusBar(false)
        setupToolbar(home_toolbar)
    }

    private fun setupViews() {
        movie_swipe_refresh.setOnRefreshListener {
            viewModel.setMovieStateEvent(MovieStateEvent.Refresh)
        }
        retry_btn.setOnClickListener {
            viewModel.setMovieStateEvent(MovieStateEvent.LoadMovies)
        }
        movieAdapter = MovieAdapter()
        movieAdapter.loadMore = {
            viewModel.setMovieStateEvent(MovieStateEvent.LoadMore)
        }
        movieAdapter.onItemClick = { movie, _ ->
            findNavController().navigate(
                R.id.action_homeFragment_to_detailsFragment2,
                bundleOf("movieId" to movie.id)
            )
        }
        popular_movies_recyclerview.layoutManager = GridLayoutManager(context, getColumnCount())
        popular_movies_recyclerview.setHasFixedSize(true)
        popular_movies_recyclerview.adapter = movieAdapter

        viewModel.setGenreStateEvent(GenreStateEvent.LoadGenres)
        viewModel.setMovieStateEvent(MovieStateEvent.LoadMovies)
    }

    private fun setupObservers() {
        viewModel.genreDataState.observe(viewLifecycleOwner, Observer {
            if (it.status == Resource.Status.SUCCESS) {
                val genres = ArrayList<Genre>(it.data!!)
                genres.add(0, Genre(-1, "All"))
                chip_tab_layout.apply {
                    for (genre in genres) {
                        this.addTab(this.newTab().setText(genre.name))
                    }
                    for (i in 0 until chip_tab_layout.tabCount) {
                        val tab = (chip_tab_layout.getChildAt(0) as ViewGroup).getChildAt(i)
                        val p = tab.layoutParams as ViewGroup.MarginLayoutParams
                        p.setMargins(MetricUtils.dpToPx(8), 0, MetricUtils.dpToPx(8), 0)
                        tab.requestLayout()
                    }
                    addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
                        override fun onTabReselected(tab: TabLayout.Tab?) {}
                        override fun onTabUnselected(tab: TabLayout.Tab?) {}
                        override fun onTabSelected(tab: TabLayout.Tab?) {
                            movieAdapter.setFilter(genres[tab!!.position].id)
                        }
                    })
                }
            }
        })

        viewModel.movieDataState.observe(viewLifecycleOwner, Observer {
            movie_swipe_refresh.isRefreshing = false
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    movieAdapter.submitList(it.data)
                    setState()
                }
                Resource.Status.LOADING -> {
                    setState(loading = true)
                }
                Resource.Status.ERROR -> {
                    setState(error = true)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home_search_menu -> {
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
            }
        }
        return false
    }

    private fun getColumnCount(): Int {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val width = (displayMetrics.widthPixels / displayMetrics.density).toInt()
        return (width / 185).coerceAtLeast(2)
    }

    private fun setState(loading: Boolean = false, error: Boolean = false) {
        popular_movies_recyclerview.visibility = View.GONE
        movie_load_error_layout.visibility = View.GONE
        movie_progressbar.visibility = View.GONE
        when {
            loading -> {
                movie_progressbar.visibility = View.VISIBLE
            }
            error -> {
                movie_load_error_layout.visibility = View.VISIBLE
            }
            else -> {
                popular_movies_recyclerview.visibility = View.VISIBLE
            }
        }
    }
}
