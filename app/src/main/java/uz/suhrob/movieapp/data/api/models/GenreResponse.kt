package uz.suhrob.movieapp.data.api.models

import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("genres")
    val genres: List<NetworkGenre>
)