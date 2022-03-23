package com.farms.farmguru.model

data class Schedules(val scheduleRowId : Int,
                     val cropId : Int,
                     val scheduleDay : Int,
                     val afterProoningDtDays : Int,
                     val medicineDetails : String,
                     val fertilizer : String,
                     val activeIngredients : String,
                     val diseaseInfection : String,
                     val phi : String,
                     val notes : String,
                     val scheduleDate : String,
                     val statusFlag:String)
