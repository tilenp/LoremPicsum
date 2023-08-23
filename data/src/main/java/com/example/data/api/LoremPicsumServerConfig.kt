package com.example.data.api

internal interface LoremPicsumServerConfig {
    val baseUrl: String
}

internal object LoremPicsumServer {
    val prod = object : LoremPicsumServerConfig {
        override val baseUrl: String = PROD_HOST
    }
}

private const val PROD_HOST = "https://picsum.photos/"