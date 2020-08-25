package uz.suhrob.movieapp.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.search_item.view.*
import uz.suhrob.movieapp.R
import uz.suhrob.movieapp.data.db.entities.Movie
import uz.suhrob.movieapp.util.UrlUtils

class SearchAdapter: ListAdapter<Movie, SearchAdapter.SearchViewHolder>(SearchDiffCallback()){
    var onItemClick: ((Movie, View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.search_item, parent, false)
        )

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            Glide.with(itemView)
                .load(UrlUtils.buildImageUrl(movie.posterPath, 2))
                .placeholder(R.drawable.poster_placeholder)
                .into(itemView.search_item_poster)
            itemView.search_item_title.text = movie.title
            itemView.setOnClickListener {
                onItemClick?.invoke(movie, itemView)
            }
            itemView.transitionName = movie.id.toString()
        }
    }

    class SearchDiffCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(p0: Movie, p1: Movie): Boolean = p0 == p1

        override fun areContentsTheSame(p0: Movie, p1: Movie): Boolean = p0.title == p1.title
    }
}