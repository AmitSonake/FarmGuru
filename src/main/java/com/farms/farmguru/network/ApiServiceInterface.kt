package com.farms.farmguru.network

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
    fun getCropVarietiesByLangId(@Query("id") id : Int): Call<ResponseBody>

    @GET("api/soilType")
    fun getSoilType(): Call<ResponseBody>
    @GET("GetSoilTypesByLangId")
    fun getSoilTypeByLangId(@Query("id") id : Int): Call<ResponseBody>

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
                     @Field( "QueDateTime") QueDateTime : String,
                     @Field( "QueText") QueText : String,
                     @Field( "ImageUrl") ImageUrl : String,
                     @Field( "VoiceUrl") VoiceUrl : String,
                     @Field(  "AssignedTo") AssignedTo : String,
                     @Field("Status") Status : String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/PlotRegistration")
    fun registerPlot(@Field("RegId") RegId : Int,
                     @Field("UserId") UserId : String,
                     @Field("FarmerName") FarmerName : String,
                     @Field("FarmerAddress") FarmerAddress : String,
                     @Field("FarmerTal") FarmerTal : String,
                     @Field("FarmerDistrict") FarmerDistrict : String,
                     @Field("FarmerState") FarmerState : String,
                     @Field("CropId") CropId : Int,
                     @Field("CropVarietyId") CropVarietyId : Int,
                     @Field("CropSeason") CropSeason : String,
                     @Field("PruningDate") PruningDate : String,
                     @Field("IrrigationSourceId") IrrigationSourceId : Int,
                     @Field("SoilTypeId") SoilTypeId : Int,
                     @Field("CropPurposeId") CropPurposeId : Int,
                     @Field("CropDistance") CropDistance : String,
                     @Field("PlotAge") PlotAge : Int,
                     @Field("PlotArea") PlotArea : Int,
                     @Field("IsPaid") IsPaid : Boolean,
                     @Field("IsTrial") IsTrial : Boolean): Call<ResponseBody>

    @GET("api/GetUserPlots")
    fun getPlotRegistered(): Call<ResponseBody>

    @GET("api/GetUserQues")
    fun getRegisteredQuestions(): Call<ResponseBody>

    @GET("api/Language")
    fun getLanguages(): Call<ResponseBody>
}


