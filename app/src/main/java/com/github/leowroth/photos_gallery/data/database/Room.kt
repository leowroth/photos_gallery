package com.github.leowroth.photos_gallery.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Dao
interface PhotoDao {
    @Query("select * from databasephoto")
    fun getPhotos(): LiveData<List<DatabasePhoto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(photos: List<DatabasePhoto>)
}

@Database(entities = [DatabasePhoto::class], version = 1)
abstract class PhotosDatabase : RoomDatabase() {
    abstract val photoDao: PhotoDao
}

private lateinit var INSTANCE: PhotosDatabase

@Provides
@Singleton
fun provideAppDatabase(@ApplicationContext context: Context): PhotosDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        PhotosDatabase::class.java,
        "photos"
    ).build()
}