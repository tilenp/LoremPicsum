package com.example.data.database.dao

import androidx.room.*
import com.example.data.model.table.ImageTable
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageTable>)

    @Query("""
        SELECT ImageTable.author 
        FROM ImageTable
        GROUP BY ImageTable.author
    """)
    fun getAuthors(): Flow<List<String>>

    @Query("""
        SELECT * 
        FROM ImageTable
    """)
    fun getImages(): Flow<List<ImageTable>>

    @Query("""
        SELECT * 
        FROM ImageTable
        WHERE ImageTable.author = :author
    """)
    fun getImagesForAuthor(author: String): Flow<List<ImageTable>>
}