package com.peraz.exampleui.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("/api/collections")
    suspend fun getCollections(): Response<CollectionsModel>

    @GET("/api/randomCollection")
    suspend fun getRandomCollections(): Response<RandomCollModel>
}