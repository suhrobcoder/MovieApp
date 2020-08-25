package uz.suhrob.movieapp.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.genre_item.view.*
import uz.suhrob.movieapp.R
import uz.suhrob.movieapp.data.db.entities.Genre
import uz.suhrob.movieapp.util.MetricUtils

class GenreAdapter(private val genres: List<Genre>)
    : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.genre_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(genres[position])
        if (position == 0) {
            (holder.itemView.layoutParams as ViewGroup.MarginLayoutParams).apply {
                marginStart = MetricUtils.dpToPx(16)
                marginEnd = MetricUtils.dpToPx(8)
            }
        }
        if (position == itemCount - 1) {
            (holder.itemView.layoutParams as ViewGroup.MarginLayoutParams).apply {
                marginEnd = MetricUtils.dpToPx(16)
                marginStart = MetricUtils.dpToPx(8)
            }
        }
    }

    override fun getItemCount(): Int = genres.size

    class GenreViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(genre: Genre) {
            itemView.genre_title_text.text = genre.name
        }
    }
}