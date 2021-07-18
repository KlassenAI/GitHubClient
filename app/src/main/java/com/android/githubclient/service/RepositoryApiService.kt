package com.android.githubclient.service

import com.android.githubclient.model.RepositoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RepositoryApiService {

    @GET("search/repositories")
    suspend fun searchRepositories(@Query("q") query: String): Response<RepositoryResponse?>
}