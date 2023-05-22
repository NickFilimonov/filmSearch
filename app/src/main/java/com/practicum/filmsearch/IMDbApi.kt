package com.practicum.filmsearch

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IMDbApi {

    @GET("/ru/API/SearchMovie/k_fkcbr584/{expression}")
    fun getFilms(@Path("expression") expression: String): Call<FilmsResponce>

}