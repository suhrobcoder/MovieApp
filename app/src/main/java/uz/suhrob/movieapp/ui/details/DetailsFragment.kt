package uz.suhrob.movieapp.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.*
import uz.suhrob.movieapp.R
import uz.suhrob.movieapp.data.api.Resource
import uz.suhrob.movieapp.extension.setupStatusBar
import uz.suhrob.movieapp.util.MetricUtils
import uz.suhrob.movieapp.util.UrlUtils

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private val viewModel : DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStatusBar(true)
        details_genre_recyclerview.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = linearLayoutManager
        }
        videos_recycler_view.layoutManager = LinearLayoutManager(context)

        val movieId = arguments?.getInt("movieId")
        viewModel.start(movieId)
        viewModel.loadVideos(movieId)
        viewModel.movie.observe(viewLifecycleOwner, Observer {
            val movie = it.data!!
            movie_title.text = movie.title
            movie_overview.text = movie.overview
            movie_rating.text = String.format(resources.getString(R.string.vote), movie.voteAverage, 10)
            votes_count.text = movie.voteCount.toString()
            movie_release_date.text = movie.getMovieReleaseYear()
            val thumbnail = Glide.with(view)
                .load(R.drawable.backdrop_placeholder)
                .apply(RequestOptions.bitmapTransform(
                    BackdropTransformation(
                        MetricUtils.dpToPx(40)
                    )
                ))
            Glide.with(view)
                .load(UrlUtils.buildImageUrl(movie.backdropPath))
                .apply(RequestOptions.bitmapTransform(
                    BackdropTransformation(
                        MetricUtils.dpToPx(40)
                    )
                ))
                .thumbnail(thumbnail)
                .into(movie_backdrop)
        })
        viewModel.movieInfo.observe(viewLifecycleOwner, Observer {
            if (it.status == Resource.Status.SUCCESS && it.data != null) {
                val movieInfo = it.data
                movie_duration.text = String.format(resources.getString(R.string.movie_length), movieInfo.runtime/60, movieInfo.runtime%60)
            }
        })
        viewModel.genres.observe(viewLifecycleOwner, Observer { genres ->
            details_genre_recyclerview.adapter =
                GenreAdapter(genres)
        })
        viewModel.videos.observe(viewLifecycleOwner, Observer {
            if (it.status == Resource.Status.SUCCESS && it.data != null) {
                trailer_header.visibility = View.VISIBLE
                val adapter = VideoAdapter(it.data.filter { videoInfo ->  videoInfo.site == "YouTube" && videoInfo.type == "Trailer" })
                adapter.onItemClick = { key ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(UrlUtils.buildYoutubeUrl(key))))
                }
                videos_recycler_view.adapter = adapter
            }
        })
    }
}
