package uz.suhrob.movieapp.data.api.models

import com.google.gson.annotations.SerializedName

data class ProductionCountries(
    @SerializedName("iso_3166_1") val iso_3166_1 : String,
    @SerializedName("name") val name : String
)