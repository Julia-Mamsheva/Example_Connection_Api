package com.example.myapplication.Adapter


import com.example.myapplication.Model.ApiModel
import retrofit2.Call
import retrofit2.http.GET

    interface ApiRequests {
        @GET("/api/character")
        fun getCharacter(): Call<ApiModel>
    }
