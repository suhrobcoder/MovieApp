package uz.suhrob.movieapp.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.video_item.view.*
import uz.suhrob.movieapp.R
import uz.suhrob.movieapp.data.api.models.VideoInfo

class VideoAdapter(private val videos: List<VideoInfo>): RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false))
    }

    override fun getItemCount() = videos.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    inner class VideoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(videoInfo: VideoInfo) {
            itemView.video_title.text = videoInfo.name
            itemView.setOnClickListener { onItemClick?.invoke(videoInfo.key) }
        }
    }
}