package uz.suhrob.movieapp.data.api.models

import com.google.gson.annotations.SerializedName

data class PopularResponse(
    @SerializedName("page") val page : Int,
    @SerializedName("results") val results : List<NetworkPopularMovie>,
    @SerializedName("total_results") val total_results : Int,
    @SerializedName("total_pages") val total_pages : Int
)