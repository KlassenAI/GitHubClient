package com.android.githubclient.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.githubclient.converters.OwnerConverter
import com.android.githubclient.model.Repository

@Database(entities = [Repository::class], version = 1, exportSchema = false)
@TypeConverters(OwnerConverter::class)
abstract class RepositoryDatabase : RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao

    companion object {
        private var instance: RepositoryDatabase? = null

        fun getInstance(application: Application): RepositoryDatabase {
            val tempInstance = instance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val inst = Room.databaseBuilder(application.applicationContext,
                        RepositoryDatabase::class.java, "repository_database")
                        .build()
                instance = inst
                return instance as RepositoryDatabase
            }
        }
    }
}