package com.example.data.hilt

import android.content.Context
import com.example.data.database.LoremPicsumDatabase
import com.example.data.database.dao.ImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object DatabaseModule {

    @Singleton
    @Provides
    fun provideLoremPicsumDatabase(context: Context): LoremPicsumDatabase {
        return LoremPicsumDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideImageDao(database: LoremPicsumDatabase): ImageDao {
        return database.getImageDao()
    }
}