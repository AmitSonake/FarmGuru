package com.farms.krushisanjivini.model

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
 val CropName: String,
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

data class CropSeason(
 val TransId: Int,
 val CropSeasonId: Int,
 val LangId: Int,
 val CropSeasonName: String
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

data class Language(
 val LangId:String,
 val LanguageName:String
)

data class PlotListing (
 val RegId : Int,
 val PlotID: String,
 val FarmerName : String,
 val FarmerAddress : String,
 val FarmerTal : String,
 val FarmerDistrict : String,
 val FarmerState : String,
 val Crop : String,
 val CropVariety : String,
 val CropSeason : String,
 val PruningDate : String,
 val SoilType : String,
 val CropPurpose : String,
 val CropDistance : String,
 val PlotAge : Int,
 val PlotArea : Int,
 val IsPaid : Boolean,


)

data class AppCurrentVersion(
 var VersionId     : Int?    = null,
 var VersionNumber : String? = null,
 var VersionDate   : String? = null
)

data class CurrentUserDetails(
 var UserName :String? =null,
 var Email :String? =null,
 var PhoneNumber :String? =null,
 var Password :String? =null,
 var ConfirmPassword :String? =null,

)

data class HomeMenuItems(
 val homeMenuTitle : String,
)

data class Notes(
 var TransId :Int,
 var NoteId :Int,
 var LangId :Int,
 var NoteText :String?,
 var ImageUrl :String?
)


data class Webinars(
 var WebinarId :Int,
 var WebinarTitle :String,
 var WebinarLink :String,
 var WebinarDateTime :String?
)

data class ServiceProviders(
 var TransId :Int,
 var ServiceProviderId :Int,
 var LangId :Int,
 var ServiceProviderName :String?,
 var Village :String?,
 var PhNo :String?,
 var Service :String?

)

data class Dealers(
 var TransId :Int,
 var DealerId :Int,
 var LangId :Int,
 var DealerName :String?,
 var Address :String?,
 var City :String?,
 var Taluka :String?,
 var District :String?,
 var PhNo :String?

)

data class AddImageUrls(
 var imageUrl:String?
)

data class AddVideoUrl(
 var videoUrl:String?
)

data class AddYoutubeUrls(
 val TransId : Int,
 val VideoUrlId : Int,
 val VideoUrlText : String,
 val ThumbnailUrl : String,
 val LangId : Int,
 val Title : String,
 val Description : String,
 val AllRecCount : Int,
)


