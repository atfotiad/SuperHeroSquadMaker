package com.atfotiad.superherosquadmaker.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {
    @SerializedName("offset")
    @Expose
    var offset: String? = null

    @SerializedName("limit")
    @Expose
    var limit: String? = null

    @SerializedName("total")
    @Expose
    var total: String? = null

    @SerializedName("count")
    @Expose
    var count: String? = null

    @SerializedName("results")
    @Expose
    var results: List<Result>? = null

}