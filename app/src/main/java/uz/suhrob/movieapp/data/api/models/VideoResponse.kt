package uz.suhrob.movieapp.data.api.models

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<VideoInfo>
)