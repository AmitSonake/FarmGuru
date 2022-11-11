package com.farms.krushisanjivini.ui.questions

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.R
import com.farms.krushisanjivini.adapters.QuestionsListAdapter
import com.farms.krushisanjivini.databinding.ActivityQuestionsListingBinding
import com.farms.krushisanjivini.model.Questions
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.utilities.CheckInternetConnection
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyQuestionListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionsListingBinding
    private var mApiService: ApiServiceInterface?= null
    private val questionList = ArrayList<Questions>()
    private var questionParsedList = ArrayList<Questions>()
    var languageCode:String? ="kn"
    var plotId=0
    var plotName :String? =""
    var userLangId:Int =1

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
        binding = ActivityQuestionsListingBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
        val userLanugage= SharedPreferencesHelper.invoke(this).getSelectedLanguage()
        if(userLanugage.equals("EN")){
            userLangId=1
        }else {
            userLangId=2
        }
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(ApiServiceInterface::class.java)
       // getQuestionsListing()
        val networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
        if(networkStatus) {
            getQuestionsListing()
        }else{
            CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),this)
        }
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE)
    }

    private fun getQuestionsListing(){
        binding.progressbar.visibility = View.VISIBLE
        mApiService!!.getRegisteredQuestions().enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    questionParsedList= stringResponse?.let { parseJson(it.toString()) }!!
                    if(questionParsedList.size>0){
                        binding.questionsListRecyclerView.adapter = QuestionsListAdapter(questionParsedList)
                    }else {
                        if(userLangId==2){
                            binding.noScheduleLabel.text="ಯಾವುದೇ ಡೇಟಾ ಲಭ್ಯವಿಲ್ಲ"
                        }else{
                            binding.noScheduleLabel.text="There is no any data available"
                        }
                        binding.noScheduleLabel.visibility = View.VISIBLE
                    }

                }else if(response.code()==401){
                    CheckInternetConnection.showSessionTimeOutDialog("User login session expired!!.",this@MyQuestionListingActivity)
                }else{

                }
                binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })

    }

    private fun parseJson(jsonResponse: String):ArrayList<Questions>{
        val jsonArray = JSONArray(jsonResponse)
        for (i in 0..jsonArray!!.length() - 1) {
            val jsonObj = jsonArray.optJSONObject(i)
            val questionId =jsonObj.optInt("QueId")
            val userId =jsonObj.optString("UserId")
            val queDateTime =jsonObj.optString("QueDateTime")
            val queText =jsonObj.optString("QueText")
            val imageUrl =jsonObj.optString("ImageUrl")
            val voiceUrl =jsonObj.optString("VoiceUrl")
            val assignedTo =jsonObj.optString("AssignedTo")
            val status =jsonObj.optString("Status")
            val answer =jsonObj.optString("Answer")
            val answeredDate =jsonObj.optString("AnswerDate")
            println("Question Text=$queText")
            questionList.add(
              Questions(questionId,userId,queDateTime,queText,imageUrl,voiceUrl,assignedTo,status,answer,answeredDate)
            )

        }

        return questionList
    }
}