package com.mufiid.pilwali2020.api

import com.mufiid.pilwali2020.models.*
import com.mufiid.pilwali2020.responses.MessageResponse
import com.mufiid.pilwali2020.responses.VerificationResponse
import com.mufiid.pilwali2020.responses.WrappedListResponses
import com.mufiid.pilwali2020.responses.WrappedResponse
import io.reactivex.rxjava3.core.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    // GET Data Tps
    @GET("tps")
    fun getDataTps(
        @Query("id") id_tps: String?
    ): Flowable<WrappedResponse<Tps>>

    // GET Data Verifikasi
    @GET("perhitungan")
    fun getVerifikasi(
        @Query("verif_tps") id_tps: String?
    ): Flowable<VerificationResponse>

    // GET CONFIG
    @GET("config")
    fun getConfig() : Flowable<WrappedResponse<Config>>

    // INSERT DAFTAR PEMILIH
    @FormUrlEncoded
    @POST("tps")
    fun inputDaftarPemilih(
        @Field("id") id_tps: String?,
        @Field("form_page") form_page: String?,
        @Field("dpt_2") dpt: Int?,
        @Field("dptb_2") dptb: Int?,
        @Field("dpk_2") dpk: Int?,
        @Field("dpktb_2") dpktb: Int?,
        @Field("difabel_2") difabel: Int?
    ): Flowable<MessageResponse>

    // INSERT DATA TPS
    @Multipart
    @POST("tps")
    fun inputDataTPS(
        @Part("id") id_tps: RequestBody?,
        @Part("form_page") form_page: RequestBody?,
        @Part foto_tps: MultipartBody.Part?,
        @Part("lati") latitude: RequestBody?,
        @Part("longi") longitude: RequestBody?,
        @Part("username") username: RequestBody?
    ): Flowable<MessageResponse>

    // DATA SUARA PASLON
    @Multipart
    @POST("perhitungan")
    fun postSuaraPaslon(
        @Part("id_tps") id_tps: RequestBody?,
        @Part("suara_tidak_sah") suara_tidak_sah: RequestBody?,
        @Part("id_paslon[]") id_paslon: List<Int>?,
        @Part("suara_sah[]") suara_sah: List<Int>?,
        @Part foto_blanko: MultipartBody.Part?,
        @Part("username") username: RequestBody?
    ): Flowable<MessageResponse>

    // UPDATE PROFILE
    @FormUrlEncoded
    @POST("user")
    fun updateProfile(
        @Field("id") id_user: String?,
        @Field("username") username: String?,
        @Field("nama") nama: String?,
        @Field("password") passwdNew: String?
    ): Flowable<MessageResponse>
}