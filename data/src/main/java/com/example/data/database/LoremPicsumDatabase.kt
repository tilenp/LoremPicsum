package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.database.dao.ImageDao
import com.example.data.model.table.ImageTable

@Database(
    entities = [
        ImageTable::class,
    ],
    version = 1
)
internal abstract class LoremPicsumDatabase : RoomDatabase() {
    abstract fun getImageDao(): ImageDao

    companion object {

        @Volatile
        private var INSTANCE: LoremPicsumDatabase? = null

        fun getInstance(context: Context): LoremPicsumDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, LoremPicsumDatabase::class.java, "LOREM_PICSUM_DATABASE")
                .build()
    }
}