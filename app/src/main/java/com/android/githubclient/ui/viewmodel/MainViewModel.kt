package com.android.githubclient.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.githubclient.db.RepositoryDao
import com.android.githubclient.db.RepositoryDatabase
import com.android.githubclient.model.Repository
import com.android.githubclient.repository.AppRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository: AppRepository

    init {
        val dao: RepositoryDao = RepositoryDatabase.getInstance(application).repositoryDao()
        appRepository = AppRepository(dao)
    }

    val favoritesRepositories = appRepository.getFavoriteRepositories()
    val searchRepositories = MutableLiveData<List<Repository>?>()

    fun insert(repository: Repository) {
        viewModelScope.launch(IO) {
            appRepository.insert(repository)
        }
    }

    fun delete(repository: Repository) {
        viewModelScope.launch(IO) {
            appRepository.delete(repository)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(IO) {
            appRepository.deleteAll()
        }
    }

    fun isExist(id: Int): Boolean = runBlocking {
        return@runBlocking appRepository.isExist(id)
    }

    fun searchRepositories(query: String) {
        viewModelScope.launch(IO) {
            try {
                val repositories = appRepository.searchRepositories(query)
                searchRepositories.postValue(repositories)
            } catch (io: IOException) {
                searchRepositories.postValue(null)
            }
        }
    }
}