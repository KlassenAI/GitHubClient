package com.android.githubclient.converters

import androidx.room.TypeConverter
import com.android.githubclient.model.Owner

class OwnerConverter {

    @TypeConverter
    fun fromOwnerToLogin(input: Owner): String {
        return input.login
    }

    @TypeConverter
    fun fromLoginToConverter(input: String): Owner {
        return Owner(input)
    }
}