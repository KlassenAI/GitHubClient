package com.android.githubclient.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "repository_table")
data class Repository(
    @SerializedName("html_url")
    val htmlUrl: String,
    @PrimaryKey
    val id: Int,
    val name: String,
    val owner: Owner,
    @SerializedName("default_branch")
    val defaultBranch: String
)