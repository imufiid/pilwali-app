package com.mufiid.pilwali2020.data.repository

import com.mufiid.pilwali2020.data.remote.AuthRemoteDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthRepositoryImpl(private val authRemoteDataSource: AuthRemoteDataSource): AuthRepository {
    override suspend fun login(userName: String, password: String) =
        authRemoteDataSource.login(userName, password)

    override suspend fun getUserByID(userID: Int) = authRemoteDataSource.getUserByID(userID)

    override suspend fun updateProfile(
        userID: RequestBody?,
        userName: RequestBody?,
        name: RequestBody?,
        pict: MultipartBody.Part?,
        newPassword: RequestBody?,
        apiKey: RequestBody?
    ) = authRemoteDataSource.updateProfile(userID, userName, name, pict, newPassword, apiKey)
}