package com.example.data.api

import com.example.data.model.dto.ImageDto
import retrofit2.http.GET

internal interface LoremPicsumApi {

    @GET("v2/list")
    suspend fun loadImages(): List<ImageDto>
}
