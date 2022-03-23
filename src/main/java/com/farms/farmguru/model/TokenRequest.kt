package com.farms.farmguru.model

import com.google.gson.annotations.SerializedName

data class TokenRequest(val username:String?,
val password:String?,
 val grant_type:String?)

data class TokenResponse (
 val access_token : String,
 val token_type : String,
 val expires_in : Int,
 val userName : String,
 val roles : String,
)

data class CropResponse(
 val CropId: String,
 val CropName: String
)

data class SoilType(
 val SoilTypeId: Int,
 val SoilTypeName: String
)

data class CropVariety(
 val CropVarietyId: Int,
 val CropVarietyName: String,
 val CropId: Int
)

data class CropPurpose(
 val CropPurposeId: String,
 val CropPurposeDesc: String
)

data class IrrigationSource(
 val IrrigationSourceId: String,
 val IrrigationSourceName: String
)

data class GardenMethod(
 val GardenMethodId: String,
 val GardenMethodName: String
)


data class PlotListing (
 val RegId : Int,
 val UserId : String,
 val FarmerName : String,
 val FarmerAddress : String,
 val FarmerTal : String,
 val FarmerDistrict : String,
 val FarmerState : String,
 val CropId : Int,
 val CropVarietyId : Int,
 val CropSeason : String,
 val PruningDate : String,
 val IrrigationSourceId : Int,
 val SoilTypeId : Int,
 val CropPurposeId : Int,
 val CropDistance : String,
 val PlotAge : Int,
 val PlotArea : Int,
 val IsPaid : Boolean,
 val IsTrial : Boolean

)