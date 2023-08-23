package com.example.core.mapper

import android.content.Context
import com.example.core.R
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorMapper @Inject constructor(
    @ApplicationContext private val context: Context,
) : Mapper<Throwable?, String> {

    override fun map(objectToMap: Throwable?): String {
        return when (objectToMap) {
            is IOException -> context.getString(R.string.Network_not_available)
            is HttpException -> context.getString(R.string.Server_error)
            else -> context.getString(R.string.Unknown_error)
        }
    }
}