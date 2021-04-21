package com.bignerdranch.android.kotlin_point.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Request {

    fun buildRetrofitConfig(): Api? {
        val httpClient: OkHttpClient.Builder = getBaseHttpConfig()
        val retrofit: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        return retrofit.build().create(Api::class.java)
    }

        private fun getBaseHttpConfig(): OkHttpClient.Builder {
        val log = HttpLoggingInterceptor()
        log.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(log)
        return okHttpClient
    }
}
