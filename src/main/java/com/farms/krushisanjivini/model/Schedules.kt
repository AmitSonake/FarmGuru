package com.farms.krushisanjivini.model

data class Schedules(val scheduleRowId : Int,
                     val cropId : Int,
                     val cropVarietyId : Int,
                     val langId : Int,
                     val scheduleDay : Int,
                     val afterProoningDtDays : Int,
                     val sprayScheduleNote : String,
                     val sprayMedicineBrand : String,
                     val sprayIngredients : String,
                     val sprayPurpose : String,
                     val basalDoseNote : String,
                     val basalDoseMedicineBrand : String,
                     val basalDoseIngredients : String,
                     val basalDosePurpose : String,
                     val noteIfAny : String,
                     val scheduleDate : String,
                     val statusFlag:String)
