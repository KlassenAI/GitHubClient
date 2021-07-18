package com.android.githubclient.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.android.githubclient.model.Repository

@Dao
interface RepositoryDao {

    @Insert(onConflict = IGNORE)
    suspend fun insert(repository: Repository)

    @Delete
    suspend fun delete(repository: Repository)

    @Query("DELETE FROM repository_table")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM repository_table WHERE id = :id)")
    suspend fun isExist(id: Int): Boolean

    @Query("SELECT * FROM repository_table")
    fun getRepositories(): LiveData<List<Repository>>
}