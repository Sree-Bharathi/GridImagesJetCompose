package com.sree.imagelistapplication.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.sree.imagelistapplication.network.Src

@Entity
data class PhotosEntity (
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id"               )
    var id              : Int?     = null,
    @SerializedName("width"            )
    var width           : Int?     = null,
    @SerializedName("height"           )
    var height          : Int?     = null,
    @SerializedName("url"              )
    var url             : String?  = null,
    @SerializedName("photographer"     )
    var photographer    : String?  = null,
    @SerializedName("photographer_url" )
    var photographerUrl : String?  = null,
    @SerializedName("photographer_id"  )
    var photographerId  : Int?     = null,
    @SerializedName("avg_color"        )
    var avgColor        : String?  = null,
    @SerializedName("src"              )
    var src             : String?     = null,
    @SerializedName("liked"            )
    var liked           : Boolean? = null,
    @SerializedName("alt"              )
    var alt             : String?  = null,
    @SerializedName("category"              )
    var category             : String?  = null


)
