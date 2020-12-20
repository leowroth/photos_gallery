package com.github.leowroth.photos_gallery.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PhotoDao {
    @Query("select * from databasephoto")
    fun getPhotos(): LiveData<MutableList<DatabasePhoto>>

    @Query("select * from databasephoto")
    fun getCurrentPhotos(): List<DatabasePhoto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllReplace(photos: List<DatabasePhoto>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllIgnore(photos: List<DatabasePhoto>)

    @Update()
    fun update(copyPhoto: DatabasePhoto)
}

@Database(entities = [DatabasePhoto::class], version = 6, exportSchema = false)
abstract class PhotosDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}