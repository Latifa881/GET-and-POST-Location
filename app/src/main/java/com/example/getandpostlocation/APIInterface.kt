package com.example.getandpostlocation

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIInterface {
    @Headers("Content-Type: application/json")
    @GET("/test")
    fun getDetails(): Call<ArrayList<Details.Data>>

    @Headers("Content-Type: application/json")
    @POST("/test/")
   // fun addNames(@Body name: String): Call<List<Names.Name>>
    fun addDetails(@Body details: Details.Data): Call<Details.Data>
}