package com.github.leowroth.photos_gallery.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

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

fun getDatabase(context: Context): PhotosDatabase {
    synchronized(PhotosDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                PhotosDatabase::class.java,
                "photos"
            ).build()
        }
    }
    return INSTANCE
}