package com.groomers.groomersvendor.retrofit

import com.groomers.groomersvendor.model.ModelGetVendor.ModelGetVendor
import com.groomers.groomersvendor.model.modelAccept.ModelAccept
import com.groomers.groomersvendor.model.modelGetBooking.ModelGetBooking
import com.groomers.groomersvendor.model.modeladdbank.ModelAddBank
import com.groomers.groomersvendor.model.modeladdhelp.ModelAddHelp
import com.groomers.groomersvendor.model.modelcategory.ModelCategory
import com.groomers.groomersvendor.model.modelcity.ModelCity
import com.groomers.groomersvendor.model.modelcreateservice.ModelCreateServiceX
import com.groomers.groomersvendor.model.modeldeleteservice.ModelDeleteService
import com.groomers.groomersvendor.model.modelhelplist.ModelHelpList
import com.groomers.groomersvendor.model.modellogin.ModelLogin
import com.groomers.groomersvendor.model.modelregister.ModelRegister
import com.groomers.groomersvendor.model.modelservice.ModelService
import com.groomers.groomersvendor.model.modelsingleservice.ModelSingleService
import com.groomers.groomersvendor.model.modelslot.ModelSlot
import com.groomers.groomersvendor.model.modelslotlist.ModelSlotList
import com.groomers.groomersvendor.model.modelstate.ModelState
import com.groomers.groomersvendor.model.modelupdateprfphoto.ModelUpdateProfPhoto
import com.groomers.groomersvendor.model.modelupdateservice.ModelUpdateService
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("role") role: String
    ): Response<ModelLogin>
 @POST("login")
    suspend fun loginWithUsername(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("role") role: String
    ): Response<ModelLogin>

    @Multipart
    @POST("register")
    suspend fun registerUser(
        @Query("name") name: String,
        @Query("mobile") mobile: String,
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("password_confirmation") passwordConfirmation: String,
        @Query("role") role: String,
        @Query("businessName") businessName: String,
        @Query("businessCategory") businessCategory: String,
        @Query("aboutBusiness") aboutBusiness: String,
        @Query("teamSize") teamSize: String,
        @Query("address") address: String,
        @Query("city") city: String,
        @Query("zipcode") zipcode: String,
        @Query("idproofType") idproofType: String,
        @Query("services") services: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("country") country: String,
        @Query("state") state: String,
//        @Query("slot_interval") slot_interval: String,      // New field added
        @Query("username") username: String,
        @Part shopAgreement: MultipartBody.Part,
        @Part idproofimage: MultipartBody.Part,
        @Query("language") language: String,
        @Query("AccountName") AccountName: String,
        @Query("AccountNumber") AccountNumber: String,
        @Query("BankName") BankName: String,
        @Query("ifsc_code") ifsc_code: String,
        @Query("BranchName") BranchName: String,
        @Query("mapUrl") mapUrl: String,

    ): Response<ModelRegister>

    @POST("city")
    suspend fun getCity(): Response<ModelCity>

    @POST("state")
    suspend fun getState(): Response<ModelState>

    @GET("getCategory")
    suspend fun getCategory(): Response<ModelCategory>

    @POST("getPostAll")
    suspend fun getServiceList(
        @Header("Authorization") authorization: String
    ): Response<ModelService>

    @POST("create_slot")
    suspend fun createSlot(
        @Header("Authorization") authorization: String,
        @Query("start_time") start_time: String,
        @Query("end_time") end_time: String,
        @Query("day") day: String,
        @Query("forService") forService: String,
        @Query("quantity") quantity: String,
        @Query("service_id") service_id: String
    ): Response<ModelSlot>


    @Multipart
    @POST("CreateServicePost")
    suspend fun createServicePost(
        @Header("Authorization") authorization: String,
        @Query("serviceName") serviceName: String,
        @Query("description") description: String,
        @Query("price") price: String,
        @Query("time") time: String,
        @Query("serviceType") serviceType: String,
        @Query("date") date: String,
        @Query("category") category: String,
        @Query("slot_time") slot_time: String,
        @Query("address") address: String,
        @Query("user_type") user_type: String,
//        @Part image: List<MultipartBody.Part>
        @Part image: MultipartBody.Part
    ): Response<ModelCreateServiceX>

    @Multipart
    @POST("updatePost")
    suspend fun updateServicePost(
        @Header("Authorization") authorization: String,
        @Query("serviceName") serviceName: String,
        @Query("description") description: String,
        @Query("price") price: String,
        @Query("time") time: String,
        @Query("serviceType") serviceType: String,
        @Query("date") date: String,
        @Query("category") category: String,
        @Query("slot_time") slot_time: String,
        @Query("address") address: String,
        @Query("user_type") user_type: String,
        @Part image: MultipartBody.Part,
        @Query("id") id: String
    ): Response<ModelUpdateService>

    @POST("deletePost")
    suspend fun deleteService(
        @Header("Authorization") authorization: String,
        @Query("id") id: String
    ): Response<ModelDeleteService>

    @POST("getSinglePost")
    suspend fun getSingleService(
        @Header("Authorization") authorization: String,
        @Query("id") id: String
    ): Response<ModelSingleService>

    @POST("acceptBooking")
    suspend fun acceptBooking(
        @Header("Authorization") authorization: String,
        @Query("booking_id") booking_id: String,
        @Query("slug") slug: String
    ): Response<ModelAccept>


    @GET("getBookings")
    suspend fun getBookings(
        @Header("Authorization") authorization: String,
        @Query("date") date : String
    ): Response<ModelGetBooking>


    @POST("getSlot")
    suspend fun getTimeSlot(
        @Header("Authorization") authorization: String,
        @Query("day") day: String?,
    ): Response<ModelSlotList>

    @GET("getQuery")
    suspend fun getHelpList(
        @Header("Authorization") authorization: String
    ): Response<ModelHelpList>


    @Multipart
    @POST("profile_picture")
    suspend fun uploadProfilePicture(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part
    ): Response<ModelUpdateProfPhoto>

    @Multipart
    @POST("profile_picture")
    suspend fun uploadCoverPicture(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part
    ): Response<ModelUpdateProfPhoto>

    @Multipart
    @POST("addHelp")
    suspend fun addHelp(
        @Header("Authorization") authorization: String,
        @Query("name") name : String,
        @Query("mobile") mobile : String,
        @Query("query") query : String,
        @Query("description") description : String,
        @Part image: MultipartBody.Part
    ): Response<ModelAddHelp>

    @POST("getUser")
    suspend fun getVendor(
        @Header("Authorization") authorization: String,
     ): Response<ModelGetVendor>

    @POST("addBank")
    suspend fun addBank(
        @Header("Authorization") authorization: String,
        @Query("AccountName") AccountName : String,
        @Query("AccountNumber") AccountNumber : String,
        @Query("BankName") BankName : String,
        @Query("ifsc_code") ifsc_code : String,
        @Query("BranchName") BranchName : String,
        @Query("UPI") upi : String,
        ): Response<ModelAddBank>


    @POST("vendorStatus")
     fun onlineOffline(
        @Header("Authorization") authorization: String,
        @Query("vendorStatus") vendorStatus: String,
    ): Call<ModelAddBank>
}