package uz.suhrob.movieapp.ui.home

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.transition.Hold
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import uz.suhrob.movieapp.R
import uz.suhrob.movieapp.data.api.Resource
import uz.suhrob.movieapp.data.db.entities.Genre
import uz.suhrob.movieapp.extension.setupStatusBar
import uz.suhrob.movieapp.extension.setupToolbar
import uz.suhrob.movieapp.util.MetricUtils

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var movieAdapter: MovieAdapter
    private val viewModel: MainViewModel by viewModels()
    private var refreshing = false
    private var loadingMore = false
    private var selectedTabPos = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setGenreStateEvent(GenreStateEvent.LoadGenres)
        viewModel.setMovieStateEvent(MovieStateEvent.LoadMovies)
        movieAdapter = MovieAdapter()
        exitTransition = Hold()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        setupViews()
        setupObservers()
        setHasOptionsMenu(true)
        setupStatusBar(false)
        setupToolbar(home_toolbar)
    }

    private fun setupViews() {
        movie_swipe_refresh.setOnRefreshListener {
            refreshing = true
            viewModel.setMovieStateEvent(MovieStateEvent.Refresh)
        }
        retry_btn.setOnClickListener {
            viewModel.setMovieStateEvent(MovieStateEvent.LoadMovies)
        }
        movieAdapter.loadMore = {
            loadingMore = true
            viewModel.setMovieStateEvent(MovieStateEvent.LoadMore)
        }
        movieAdapter.onItemClick = { movie, view ->
            val movieTransitionName = getString(R.string.movie_details_transition)
            val extras = FragmentNavigatorExtras(view to movieTransitionName)
            val directions = HomeFragmentDirections.actionHomeFragmentToDetailsFragment2(movie.id)
            findNavController().navigate(
                directions,
                extras
            )
        }
        popular_movies_recyclerview.layoutManager = GridLayoutManager(context, getColumnCount())
        popular_movies_recyclerview.setHasFixedSize(true)
        popular_movies_recyclerview.adapter = movieAdapter
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
                    visibility = View.VISIBLE
                    for (i in 0 until chip_tab_layout.tabCount) {
                        val tab = (chip_tab_layout.getChildAt(0) as ViewGroup).getChildAt(i)
                        val p = tab.layoutParams as ViewGroup.MarginLayoutParams
                        p.setMargins(MetricUtils.dpToPx(8), 0, MetricUtils.dpToPx(8), 0)
                        tab.requestLayout()
                    }
                    setScrollPosition(selectedTabPos, 0F, false)
                    getTabAt(selectedTabPos)?.select()
                    addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
                        override fun onTabReselected(tab: TabLayout.Tab?) {}
                        override fun onTabUnselected(tab: TabLayout.Tab?) {}
                        override fun onTabSelected(tab: TabLayout.Tab?) {
                            movieAdapter.setFilter(genres[tab!!.position].id)
                            selectedTabPos = tab.position
                        }
                    })
                }
            }
        })

        viewModel.movieDataState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    movieAdapter.submitList(it.data)
                    movie_swipe_refresh.isRefreshing = false
                    loadingMore = false
                    setState()
                }
                Resource.Status.LOADING -> {
                    if (!refreshing && !loadingMore) {
                        setState(loading = true)
                    }
                }
                Resource.Status.ERROR -> {
                    movie_swipe_refresh.isRefreshing = false
                    if (!loadingMore) {
                        setState(error = true)
                    }
                    loadingMore = false
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
        refreshing = false
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
