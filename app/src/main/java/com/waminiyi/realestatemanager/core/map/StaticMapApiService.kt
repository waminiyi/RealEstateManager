package com.waminiyi.realestatemanager.core.map

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface StaticMapApiService {
    @GET("/maps/api/staticmap")
     fun getStaticMap(
        @Query("center") center: String,
        @Query("zoom") zoom: Int,
        @Query("size") size: String,
        @Query("key") key: String
    ): Call<ResponseBody>
}