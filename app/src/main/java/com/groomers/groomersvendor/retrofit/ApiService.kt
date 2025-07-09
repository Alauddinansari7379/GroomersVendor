package com.groomers.groomersvendor.retrofit

import com.example.groomers.model.modelvendorrating.ModelVendorRating
import com.groomers.groomersvendor.model.ModelGetVendor.ModelGetVendor
import com.groomers.groomersvendor.model.modelAccept.ModelAccept
import com.groomers.groomersvendor.model.modelEarning.ModelEarning
import com.groomers.groomersvendor.model.modelForgot.ModelForgot
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
import com.groomers.groomersvendor.model.modeluserexist.ModelUserExist
import com.groomers.groomersvendor.model.rating.Rating
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
        @Query("service_id") service_id: String,
        @Query("interval") interval: String
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
        @Query("address") address: String,
        @Query("user_type") user_type: String,
//        @Part image: List<MultipartBody.Part>
        @Part image: MultipartBody.Part,
        @Query("discount") discount: String,
        @Query("start_time") start_time: String,
        @Query("end_time") end_time: String,
        @Query("quantity") quantity: Int,
        @Query("Day1") Day1: String,
        @Query("Day2") Day2: String,
        @Query("Day3") Day3: String,
        @Query("Day4") Day4: String,
        @Query("Day5") Day5: String,
        @Query("Day6") Day6: String,
        @Query("Day7") Day7: String,

    ): Response<ModelCreateServiceX>


    @Multipart
    @POST("updatePost")
    suspend fun updateServicePost(
        @Header("Authorization") authorization: String,
        @Query("id") id: String,
        @Query("serviceName") serviceName: String,
        @Query("description") description: String,
        @Query("price") price: String,
        @Query("time") time: String,
        @Query("serviceType") serviceType: String,
        @Query("date") date: String,
        @Query("discount") discount: String,
        @Query("user_type") user_type: String,
//        @Query("category") category: String,
//        @Query("address") address: String,
        @Query("start_time") startTime: String,
        @Query("end_time") endTime: String,
        @Query("quantity") quantity: Int,
        @Query("Day1") day1: Int,
        @Query("Day2") day2: Int,
        @Query("Day3") day3: Int,
        @Query("Day4") day4: Int,
        @Query("Day5") day5: Int,
        @Query("Day6") day6: Int,
        @Query("Day7") day7: Int,
        @Part image: MultipartBody.Part,
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

    @POST("booking_status_change")
    suspend fun bookingStatusChange(
        @Header("Authorization") authorization: String,
        @Query("booking_id") booking_id: String,
        @Query("slug") slug: String
    ): Response<ModelAccept>


    @GET("getBookings")
    suspend fun getBookings(
        @Header("Authorization") authorization: String,
        @Query("date") date: String
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
        @Part vendorCoverImage: MultipartBody.Part
    ): Response<ModelUpdateProfPhoto>

    @Multipart
    @POST("addHelp")
    suspend fun addHelp(
        @Header("Authorization") authorization: String,
        @Query("name") name: String,
        @Query("mobile") mobile: String,
        @Query("query") query: String,
        @Query("description") description: String,
        @Part image: MultipartBody.Part
    ): Response<ModelAddHelp>

    @POST("getUser")
    suspend fun getVendor(
        @Header("Authorization") authorization: String,
    ): Response<ModelGetVendor>

    @POST("addBank")
    suspend fun addBank(
        @Header("Authorization") authorization: String,
        @Query("AccountName") AccountName: String,
        @Query("AccountNumber") AccountNumber: String,
        @Query("BankName") BankName: String,
        @Query("ifsc_code") ifsc_code: String,
        @Query("BranchName") BranchName: String,
        @Query("UPI") upi: String,
    ): Response<ModelAddBank>


    @POST("vendorStatus")
    fun onlineOffline(
        @Header("Authorization") authorization: String,
        @Query("VendorStatus") vendorStatus: String,
    ): Call<ModelAddBank>

    @POST("vendor_earning")
    fun vendorEarning(
        @Header("Authorization") authorization: String,
    ): Call<ModelEarning>

    @POST("forgotPassword")
    fun forgotPassword(
        @Query("email") email: String,
        @Query("role") role: String,
    ): Call<ModelForgot>

    @POST("reset_password")
    fun resetPassword(
        @Query("email") email: String,
        @Query("role") role: String,
        @Query("password") password: String,
    ): Call<ModelForgot>

    @POST("customer_rating")
    suspend fun customerRating(
        @Header("Authorization") authorization: String,
        @Query("bookingId") bookingId: String,
        @Query("rating") rating: String,
        @Query("comments") comments: String,
    ): Response<Rating>

    @POST("checkUserExist")
    suspend fun checkUserExist(
        @Query("email") email: String,
        @Query("username") username: String,
    ): Response<ModelUserExist>

    @POST("getAllVendorRatings")
    suspend fun getAllVendorRatings(
        @Header("Authorization") authorization: String,
        @Query("vendor_id") vendor_id: String,
    ): Response<ModelVendorRating>
}