package com.bignerdranch.android.kotlin_point.api

import com.bignerdranch.android.kotlin_point.data.MapResponse
import retrofit2.Call
import retrofit2.http.GET

interface Api {

    companion object{
        const val BASE_URL : String = "https://2fjd9l3x1l.api.quickmocker.com/"
    }

    @GET("kyiv/places")
    fun getMapResult(): Call<MapResponse?>?
}