package com.android.githubclient.repository

import androidx.lifecycle.LiveData
import com.android.githubclient.db.RepositoryDao
import com.android.githubclient.model.Repository
import com.android.githubclient.service.RepositoryApiService
import com.android.githubclient.service.RetrofitInstance.service

class AppRepository(private val dao: RepositoryDao) {
    private val apiService: RepositoryApiService = service

    fun getFavoriteRepositories() : LiveData<List<Repository>> {
        return dao.getRepositories()
    }

    suspend fun insert(repository: Repository) {
        dao.insert(repository)
    }

    suspend fun delete(repository: Repository) {
        dao.delete(repository)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun isExist(id: Int): Boolean {
        return dao.isExist(id)
    }

    suspend fun searchRepositories(query: String): List<Repository>? {
        val response = apiService.searchRepositories(query).body()
        return response?.repositories
    }
}