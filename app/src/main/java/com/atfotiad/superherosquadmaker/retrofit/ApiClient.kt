package com.atfotiad.superherosquadmaker.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private const val BASE_URL = "https://gateway.marvel.com/v1/public/"
    const val API_KEY = "fd8305a90f6de37776a56429d280578c"
    const val ts = "1"
    const val md5hash = "dfabe3f090fc7c3c0c13b118438345a7"
    const val LIMIT = 20
    const val OFFSET =0


    fun getClient():ApiInterface{


        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiInterface::class.java)
    }
}