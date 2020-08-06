package uz.suhrob.movieapp.data.api.models

import com.google.gson.annotations.SerializedName

data class NetworkGenre(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)