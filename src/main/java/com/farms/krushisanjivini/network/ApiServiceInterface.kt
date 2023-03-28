package com.farms.krushisanjivini.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServiceInterface {

    @GET("top-headlines")
    fun getLatestNews(@Query("sources") source :String,
                      @Query("apiKey")apiKey: String ): Call<ResponseBody>


    @GET("v1/forecast.json")
    fun getWeatherData(@Query("key") key :String,
                       @Query("q")q: String ,
                       @Query("days")days: String ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("token")
    fun getToken(@Field("username") username :String,
                 @Field("password")password: String,
                 @Field("grant_type")grant_type: String ): Call<ResponseBody>

    @GET("api/crop")
    fun getCrops(): Call<ResponseBody>
    @GET("GetCropsByLangId")
    fun GetCropsByLangId(@Query("id") id : Int): Call<ResponseBody>

    @GET("api/GetCropVarietyByCropId")
    fun getCropVariety(@Query("id") id : Int): Call<ResponseBody>
    @GET("GetCropVarietiesByLangId")
    fun getCropVarietiesByLangId(@Query("langId") langId : Int,@Query("cropId") cropId : Int): Call<ResponseBody>

    @GET("api/soilType")
    fun getSoilType(): Call<ResponseBody>
    @GET("GetSoilTypesByLangId")
    fun getSoilTypeByLangId(@Query("id") id : Int): Call<ResponseBody>

    @GET("GetCropSeasonsByLangId")
    fun getCropSeasonsByLangId(@Query("langid") id : Int,@Query("cropId") cropId : Int): Call<ResponseBody>
    //fun getCropSeasonsByLangId(@Query("langId") langId : Int,@Query("cropId") cropId : Int): Call<ResponseBody>

    @GET("api/IrrigationSource")
    fun getIrrigationSource(): Call<ResponseBody>
    @GET("GetIrrigationSourcesByLangId")
    fun getIrrigationSourceBylangID(@Query("id") id : Int): Call<ResponseBody>


    @GET("api/CropPurpose")
    fun getCropPurpose(): Call<ResponseBody>
    @GET("GetCropPurposesByLangId")
    fun getCropPurposeBylangID(@Query("id") id : Int): Call<ResponseBody>

    @GET("api/GetPlotSchedule")
    fun getSchedules(@Query("id") id : Int, @Query("langId") langId : Int): Call<ResponseBody>

    @DELETE("api/PlotRegistration")
    fun deletePlot(@Query("id") id : Int
                  ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/Question")
    fun registerQuestion(@Field("QueId") QueId : Int,
                     @Field("UserId") UserId : String,
                     @Field( "QueDateTime") QueDateTime : String?,
                     @Field( "QueText") QueText : String?,
                     @Field( "ImageUrl") ImageUrl : String?,
                     @Field( "VoiceUrl") VoiceUrl : String?,
                     @Field(  "AssignedTo") AssignedTo : String?,
                     @Field("Status") Status : String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/PlotRegistration")
    fun registerPlot(@Field("RegId") RegId: Int,
                     @Field("UserId") UserId: String,
                     @Field("FarmerName") FarmerName: String,
                     @Field("FarmerAddress") FarmerAddress: String,
                     @Field("FarmerTal") FarmerTal: String,
                     @Field("FarmerDistrict") FarmerDistrict: String,
                     @Field("FarmerState") FarmerState: String,
                     @Field("CropId") CropId: Int,
                     @Field("CropVarietyId") CropVarietyId: Int,
                     @Field("CropSeasonId") CropSeason: Int,
                     @Field("PruningDate") PruningDate: String,
                     @Field("IrrigationSourceId") IrrigationSourceId: Int,
                     @Field("SoilTypeId") SoilTypeId: Int,
                     @Field("CropPurposeId") CropPurposeId: Int,
                     @Field("CropDistance") CropDistance: String,
                     @Field("PlotAge") PlotAge: Int,
                     @Field("PlotArea") PlotArea: Int,
                     @Field("IsPaid") IsPaid: Boolean,
                     @Field("IsTrial") IsTrial: Boolean): Call<ResponseBody>

    @GET("api/GetUserPlots")
    fun getPlotRegistered(@Query("langId") langId : Int): Call<ResponseBody>

    @GET("api/GetPlotDiary")
    fun getPlotDiary(@Query("id") id : Int, @Query("langId") langId : Int): Call<ResponseBody>

    @GET("api/GetUserQues")
    fun getRegisteredQuestions(): Call<ResponseBody>

    @GET("api/Language")
    fun getLanguages(): Call<ResponseBody>

    @GET("api/CurrentAppVersion")
    fun getAppCurrentVersion(): Call<ResponseBody>

    @GET("api/CurrentUserDetails")
    fun getAppCurrentUserDetails(): Call<ResponseBody>

    @GET("GetNotesByLangId")
    fun getNotesBylangID(@Query("id") id : Int): Call<ResponseBody>

    @GET("GetNewNotesByLangId")
    fun getNewNotesBylangID(@Query("langId") langId : Int,@Query("maxReadId") maxReadId : Int): Call<ResponseBody>

    @GET("GetLastNotesByLangId")
    fun getLastNotesBylangID(@Query("langId") langId : Int,@Query("maxReadId") maxReadId : Int,
                             @Query("lastReadId") lastReadId : Int): Call<ResponseBody>
    @GET("GetUpcomingWebinarsByLangId")
    fun getWebinarsByLangId(@Query("langId") langId : Int): Call<ResponseBody>

    @GET("GetServiceProvidersByLangId")
    fun getServiceProvidersByLangId(@Query("langId") langId : Int): Call<ResponseBody>

    @GET("GetDealersByLangId")
    fun getAgreeInputDealersByLangId(@Query("langId") langId : Int): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/Account/RegisterFarmerWithOtp")
    fun registerUser(@Field("UserName") UserName : String,
                     @Field("Email") Email : String,
                     @Field("PhoneNumber") PhoneNumber : String,
                     @Field("Password") Password : String,
                     @Field("ConfirmPassword") ConfirmPassword : String,
                     @Field("OTP") OTP : String): Call<ResponseBody>

   // @FormUrlEncoded
    @POST("api/Account/sendRegOtp")
    fun sendRegOtp(@Query("phno") phno : String):Call<ResponseBody>
}


