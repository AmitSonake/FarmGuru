package com.farms.krushisanjivini.ui.mydiary

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.R
import com.farms.krushisanjivini.adapters.DiarySchedulesListAdapter
import com.farms.krushisanjivini.databinding.ActivityMyDiaryScheduleListingBinding
import com.farms.krushisanjivini.model.Schedules
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.utilities.CheckInternetConnection
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyDiaryScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyDiaryScheduleListingBinding
    private var mApiService: ApiServiceInterface?= null
    private val scheduleList = ArrayList<Schedules>()
    private var scheduleParsedList = ArrayList<Schedules>()
    private var languageCode:String? ="kn"
    private var plotId=0
    private var userLangId=1
    private var isTrial:Boolean = false

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish() // back to the parent activity.
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDiaryScheduleListingBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.attributes.windowAnimations = R.anim.bounce
        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            plotId=bundle.getInt("RegID")
            //isTrial = bundle.getBoolean("isTrial")
          /*  if(isTrial){
                showSubscriptionAlert(this,"Your trail period will expire soon," +
                        " please do subscribe it")
            }*/
            println("PlotRegistered ID= $plotId")
           // plotName=bundle.getString("cropName")
            val message = bundle.getString("LanguageCode") // 1
            languageCode=message
            println("language Code = $message")

        }
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(ApiServiceInterface::class.java)
        val networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
        if(networkStatus) {
            getScheduleListing()
        }else{
            CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),this)
        }
        getWindow().setFlags(
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
            mApiService!!.getPlotDiary(plotId,userLangId).enqueue(object:
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.code()==200||response.code()==201|| response.code()==202){
                        val stringResponse = response.body()?.string()
                        //binding.scheduleLabel.text=stringResponse
                        println("$stringResponse")
                        scheduleParsedList= stringResponse?.let { parseJson(it) }!!
                        if(scheduleParsedList.size>0){
                            binding.plotsListRecyclerView.adapter = DiarySchedulesListAdapter(scheduleParsedList)
                        }else {
                            if(userLangId==2){
                                binding.noScheduleLabel.text="ಈ ಪ್ಲಾಟ್‌ಗೆ ವೇಳಾಪಟ್ಟಿ ಲಭ್ಯವಿಲ್ಲ"
                            }else{
                                binding.noScheduleLabel.text="Schedule is not available for this plot"
                            }

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
            val cropVarietyId =jsonObj.optInt("CropVarietyId")
            val langId =jsonObj.optInt("LangId")
            val scheduleDay =jsonObj.optInt("ScheduleDay")
            val afterProoningDtDays =jsonObj.optInt("AfterProoningDtDays")
            val scheduleTextString =jsonObj.optString("SprayScheduleNote")
            /*  val schduleString:String=scheduleText.toString()
              val resultString:String= *//*schduleString.replace("\n", " ")*//* schduleString.replace("\n", System.getProperty("line.separator"));

            val scheduleTextString:String= resultString.replace("\r", " ")*/
            val sprayMedicineBrand =jsonObj.optString("SprayMedicineBrand")
            val sprayIngredients =jsonObj.optString("SprayIngredients")
            val sprayPurpose =jsonObj.optString("SprayPurpose")
            val basalDoseNote =jsonObj.optString("BasalDoseNote")
            val basalDoseMedicineBrand =jsonObj.optString("BasalDoseMedicineBrand")
            val basalDoseIngredients =jsonObj.optString("BasalDoseIngredients")
            val basalDosePurpose =jsonObj.optString("BasalDosePurpose")
            val noteIfAny =jsonObj.optString("NoteIfAny")
            val scheduleDate =jsonObj.optString("ScheduleDate")
            val statusFlag =jsonObj.optString("StatusFlag")


            /* scheduleList.add(Schedules(scheduleRowId,cropId,scheduleDay,afterProoningDtDays,
             medicineDetails,fertilizer,activeIngredients,diseaseInfection,phi,notes,scheduleDate,statusFlag))*/

            scheduleList.add(Schedules(scheduleRowId,cropId,cropVarietyId,langId,scheduleDay,afterProoningDtDays,
                scheduleTextString,sprayMedicineBrand,sprayIngredients,sprayPurpose,
                basalDoseNote,basalDoseMedicineBrand,basalDoseIngredients,basalDosePurpose,noteIfAny,scheduleDate,statusFlag))

        }

        return scheduleList
    }


    /* private fun fromHtml(html: String?): Spanned? {
         return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
             Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
         } else {
             Html.fromHtml(html)
         }
     }*/
}