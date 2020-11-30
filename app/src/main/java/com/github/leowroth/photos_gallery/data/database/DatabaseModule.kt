package com.github.leowroth.photos_gallery.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): PhotosDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PhotosDatabase::class.java,
            "photos"
        ).build()
    }
}