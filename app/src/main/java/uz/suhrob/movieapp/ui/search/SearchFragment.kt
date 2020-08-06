package uz.suhrob.movieapp.ui.search

import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import uz.suhrob.movieapp.R
import uz.suhrob.movieapp.data.api.Resource
import uz.suhrob.movieapp.extension.setupStatusBar
import uz.suhrob.movieapp.extension.setupToolbar
import uz.suhrob.movieapp.ui.BaseFragment
import uz.suhrob.movieapp.util.MetricUtils


@AndroidEntryPoint
class SearchFragment: BaseFragment() {
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_search)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasInitializedRootView) {
            setupViews()
            setHasOptionsMenu(true)
        }
        setupStatusBar(false)
        setupToolbar(search_toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.queryHint = "Search Movie"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQuery(query)
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
        searchAdapter.onItemClick = { movie ->
            findNavController().navigate(
                R.id.action_searchFragment_to_detailsFragment2,
                bundleOf("movieId" to movie.id))
        }

        search_recyclerview.adapter = searchAdapter
        search_recyclerview.addItemDecoration(getRecyclerViewDivider())
        viewModel.searchDataState.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                Resource.Status.SUCCESS -> {
                    searchAdapter.submitList(it.data)
                    search_progressbar.visibility = View.GONE
                }
                Resource.Status.LOADING -> {
                    search_progressbar.visibility = View.VISIBLE

                }
                Resource.Status.ERROR -> {
                    Toast.makeText(context, "Yuklashda xatolik", Toast.LENGTH_SHORT).show()
                    search_progressbar.visibility = View.GONE
                }
            }
        })
    }

    private fun getRecyclerViewDivider(): DividerItemDecoration {
        val divider = resources.getDrawable(R.drawable.divider)
        val inset = MetricUtils.dpToPx(72)
        val instDivider = InsetDrawable(divider, inset, 4, 4, 0)
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(instDivider)
        return itemDecoration
    }
}