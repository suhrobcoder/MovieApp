package uz.suhrob.movieapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.movie_item.view.*
import uz.suhrob.movieapp.R
import uz.suhrob.movieapp.data.db.entities.Movie
import uz.suhrob.movieapp.util.MetricUtils
import uz.suhrob.movieapp.util.UrlUtils

class MovieAdapter
    : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(
    MovieDiffCallback()
) {
    var onItemClick: ((Movie, ImageView) -> Unit)? = null
    var loadMore: (() -> Unit)? = null
    private val fullList = ArrayList<Movie>()
    private var filterId = -1

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MovieViewHolder =
        MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false))

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
        if (itemCount == position + 1) {
            loadMore?.invoke()
        }
    }

    override fun submitList(list: List<Movie>?) {
        super.submitList(list)
        fullList.clear()
        fullList.addAll(list!!.toList())
        filter()
    }

    fun setFilter(id: Int) {
        filterId = id
        filter()
    }

    private fun filter() {
        val list = ArrayList<Movie>()
        if (filterId == -1) {
            list.addAll(fullList)
        } else {
            list.addAll(fullList.filter { it.genreIds.contains(filterId) })
        }
        super.submitList(list)
    }

    inner class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            itemView.posterImageView.apply {
                Glide.with(itemView)
                    .load(UrlUtils.buildImageUrl(movie.posterPath))
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(MetricUtils.dpToPx(30))))
                    .placeholder(R.drawable.poster_placeholder)
                    .into(this)
                transitionName = movie.posterPath
            }
            itemView.titleText.text = movie.title
            itemView.ratingText.text = movie.voteAverage.toString()
            itemView.setOnClickListener {
                onItemClick?.invoke(movie, itemView.posterImageView)
            }
            itemView.transitionName = movie.id.toString()
        }
    }

    class MovieDiffCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(p0: Movie, p1: Movie): Boolean = p0 == p1

        override fun areContentsTheSame(p0: Movie, p1: Movie): Boolean = p0.title == p1.title
    }
}