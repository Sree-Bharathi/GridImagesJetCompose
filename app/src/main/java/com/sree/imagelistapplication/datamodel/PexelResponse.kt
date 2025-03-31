package com.sree.imagelistapplication.network
import com.google.gson.annotations.SerializedName


data class PexelResponse (

  @SerializedName("page"          ) var page         : Int?              = null,
  @SerializedName("per_page"      ) var perPage      : Int?              = null,
  @SerializedName("photos"        ) var photos       : ArrayList<Photos> = arrayListOf(),
  @SerializedName("total_results" ) var totalResults : Int?              = null,
  @SerializedName("next_page"     ) var nextPage     : String?           = null

)