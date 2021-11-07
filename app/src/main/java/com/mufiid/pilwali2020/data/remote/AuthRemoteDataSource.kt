package com.mufiid.pilwali2020.data.remote

import androidx.lifecycle.LiveData
import com.mufiid.pilwali2020.data.entity.User
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AuthRemoteDataSource {
    suspend fun login(userName: String, password: String) : LiveData<User?>
    suspend fun getUserByID(userID: Int) : LiveData<User?>
    suspend fun updateProfile(
        userID: RequestBody?,
        userName: RequestBody?,
        name: RequestBody?,
        pict: MultipartBody.Part?,
        newPassword: RequestBody?,
        apiKey: RequestBody?
    ) : LiveData<String>
}