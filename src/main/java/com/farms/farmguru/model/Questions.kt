package com.farms.farmguru.model

data class Questions(
    val queId : Int,
    val userId : String,
    val queDateTime : String,
    val queText : String,
    val imageUrl : String,
    val voiceUrl : String,
    val assignedTo : String,
    val status : String
)