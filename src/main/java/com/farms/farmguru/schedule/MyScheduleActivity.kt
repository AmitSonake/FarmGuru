package com.farms.farmguru.schedule

import android.os.Bundle
import android.view.View
import android.view.WindowManager
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
import android.text.Html

import android.os.Build

import android.text.Spanned




class MyScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyScheduleListingBinding
    private var mApiService: ApiServiceInterface?= null
    private val scheduleList = ArrayList<Schedules>()
    private var scheduleParsedList = ArrayList<Schedules>()
    private var languageCode:String? ="kn"
    private var plotId=0
    private var userLangId=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyScheduleListingBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        window.attributes.windowAnimations = R.anim.bounce
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
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE)
    }

    private fun getScheduleListing(){
        val userLanugage= SharedPreferencesHelper.invoke(this).getSelectedLanguage()
        if(userLanugage.equals("EN")){
            userLangId=1
        }else {
            userLangId=2
        }
        binding.progressbar.visibility = View.VISIBLE
            mApiService!!.getSchedules(plotId,userLangId).enqueue(object:
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.code()==200||response.code()==201|| response.code()==202){
                        val stringResponse = response.body()?.string()
                        //binding.scheduleLabel.text=stringResponse
                        println("$stringResponse")
                        scheduleParsedList= stringResponse?.let { parseJson(it) }!!
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
        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.optJSONObject(i)
            val scheduleRowId =jsonObj.optInt("ScheduleRowId")
            val cropId =jsonObj.optInt("CropId")
            val langId =jsonObj.optInt("LangId")
            val scheduleDay =jsonObj.optInt("ScheduleDay")
            val afterProoningDtDays =jsonObj.optInt("AfterProoningDtDays")
            val scheduleText =jsonObj.optString("ScheduleText")
            val schduleString:String=scheduleText.toString()
            val resultString:String= /*schduleString.replace("\n", " ")*/ schduleString.replace("\n", System.getProperty("line.separator"));

            val scheduleTextString:String= resultString.replace("\r", " ")
            /*val medicineDetails =jsonObj.optString("MedicineDetails")
            val fertilizer =jsonObj.optString("Fertilizer")
            val activeIngredients =jsonObj.optString("ActiveIngredients")
            val diseaseInfection =jsonObj.optString("DiseaseInfection")
            val phi =jsonObj.optString("phi")
            val notes =jsonObj.optString("notes")*/
            val scheduleDate =jsonObj.optString("ScheduleDate")
            val statusFlag =jsonObj.optString("StatusFlag")


           /* scheduleList.add(Schedules(scheduleRowId,cropId,scheduleDay,afterProoningDtDays,
            medicineDetails,fertilizer,activeIngredients,diseaseInfection,phi,notes,scheduleDate,statusFlag))*/

            scheduleList.add(Schedules(scheduleRowId,cropId,langId,scheduleDay,afterProoningDtDays,
                scheduleTextString,scheduleDate,statusFlag))

        }

        return scheduleList
    }


    private fun fromHtml(html: String?): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(html)
        }
    }
}