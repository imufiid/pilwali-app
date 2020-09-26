package com.mufiid.pilwali2020.api

import com.mufiid.pilwali2020.models.Paslon
import com.mufiid.pilwali2020.models.Tps
import com.mufiid.pilwali2020.models.User
import com.mufiid.pilwali2020.responses.MessageResponse
import com.mufiid.pilwali2020.responses.WrappedListResponses
import com.mufiid.pilwali2020.responses.WrappedResponse
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface IApiService {

    // LOGIN
    @FormUrlEncoded
    @POST("user")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Flowable<WrappedResponse<User>>

    // GET Jumlah Pemilih
    @GET("")
    fun getCountVoter(
        @Path("id_tps") id_tps: String?
    ): Flowable<WrappedResponse<Tps>>

    // GET User By Id
    @GET("user")
    fun getUserById(
        @Query("id") id_user: String?
    ): Flowable<WrappedResponse<User>>

    // GET Data Paslon
    @GET("paslon")
    fun getPaslon(): Flowable<WrappedListResponses<Paslon>>

    // GWT Data Tps
    @GET("tps")
    fun getDataTps(
        @Query("id") id_tps: String?
    ): Flowable<WrappedResponse<Tps>>


}