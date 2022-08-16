package com.farms.farmguru.ui.questions

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.farmguru.R
import com.farms.farmguru.adapters.QuestionsListAdapter
import com.farms.farmguru.databinding.ActivityQuestionsListingBinding
import com.farms.farmguru.model.Questions
import com.farms.farmguru.network.ApiClient
import com.farms.farmguru.network.ApiServiceInterface
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsListingBinding.inflate(layoutInflater)
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
        getQuestionsListing()

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
            println("Question Text=$queText")
            questionList.add(
              Questions(questionId,userId,queDateTime,queText,imageUrl,voiceUrl,assignedTo,status)
            )

        }

        return questionList
    }
}