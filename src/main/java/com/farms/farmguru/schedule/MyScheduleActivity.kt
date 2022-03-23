package com.farms.farmguru.schedule

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.farmguru.R
import com.farms.farmguru.adapters.SchedulesListAdapter
import com.farms.farmguru.databinding.ActivityMyScheduleListingBinding
import com.farms.farmguru.model.Schedules
import com.farms.farmguru.network.ApiClient
import com.farms.farmguru.network.ApiServiceInterface
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyScheduleListingBinding
    private var mApiService: ApiServiceInterface?= null
    private val scheduleList = ArrayList<Schedules>()
    private var scheduleParsedList = ArrayList<Schedules>()
    var languageCode:String? ="kn"
    var plotId=0
    var plotName :String? =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyScheduleListingBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        getWindow().getAttributes().windowAnimations = R.anim.bounce;
        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            plotId=bundle.getInt("RegID")
            //plotId = Integer.parseInt(plotsId)
            println("PlotRegistered ID= $plotId")
           // plotName=bundle.getString("cropName")
            val message = bundle.getString("LanguageCode") // 1
            languageCode=message
            println("language Code = $message")

        }
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(ApiServiceInterface::class.java)
        getScheduleListing()

    }

    private fun getScheduleListing(){
        binding.progressbar.visibility = View.VISIBLE
            mApiService!!.getSchedules(plotId).enqueue(object:
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.code()==200||response.code()==201|| response.code()==202){
                        val stringResponse = response.body()?.string()
                        println("$stringResponse")
                        scheduleParsedList= stringResponse?.let { parseJson(it.toString()) }!!
                        if(scheduleParsedList.size>0){
                            binding.plotsListRecyclerView.adapter = SchedulesListAdapter(scheduleParsedList)
                        }else {
                            binding.noScheduleLabel.visibility = View.VISIBLE
                        }

                    }
                    binding.progressbar.visibility = View.GONE
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println(t.message)
                }
            })

    }

    private fun parseJson(jsonResponse: String):ArrayList<Schedules>{
        val jsonArray = JSONArray(jsonResponse)
        for (i in 0..jsonArray!!.length() - 1) {
            val jsonObj = jsonArray.optJSONObject(i)
            val scheduleRowId =jsonObj.optInt("ScheduleRowId")
            val cropId =jsonObj.optInt("CropId")
            val scheduleDay =jsonObj.optInt("ScheduleDay")
            val afterProoningDtDays =jsonObj.optInt("AfterProoningDtDays")
            val medicineDetails =jsonObj.optString("MedicineDetails")
            val fertilizer =jsonObj.optString("Fertilizer")
            val activeIngredients =jsonObj.optString("ActiveIngredients")
            val diseaseInfection =jsonObj.optString("DiseaseInfection")
            val phi =jsonObj.optString("phi")
            val notes =jsonObj.optString("notes")
            val scheduleDate =jsonObj.optString("ScheduleDate")
            val statusFlag =jsonObj.optString("StatusFlag")


            scheduleList.add(Schedules(scheduleRowId,cropId,scheduleDay,afterProoningDtDays,
            medicineDetails,fertilizer,activeIngredients,diseaseInfection,phi,notes,scheduleDate,statusFlag))

        }

        return scheduleList
    }
}