package com.atfotiad.superherosquadmaker.retrofit

import com.atfotiad.superherosquadmaker.model.MarvelResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("characters")
    fun getResults(@Query("apikey") apiKey: String?,
                   @Query("ts") ts: String?,
                   @Query("hash") hash: String?,
                   @Query("limit") limit: Int,
                   @Query("offset") offset: Int ) : Call<MarvelResponse?>?

}