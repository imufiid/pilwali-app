package com.mufiid.pimpinan.api

import com.mufiid.pimpinan.models.User
import com.mufiid.pimpinan.response.WrappedResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface IApiService {

    // LOGIN
    @FormUrlEncoded
    @POST("user")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Observable<WrappedResponse<User>>

    // GET User By Id
    @GET("user")
    fun getUserById(
        @Query("id") id_user: String?
    ): Observable<WrappedResponse<User>>

}