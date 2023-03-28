package com.farms.krushisanjivini.notification

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.R
import com.farms.krushisanjivini.adapters.NotificationListAdapter
import com.farms.krushisanjivini.databinding.ActivityNotificationListingBinding
import com.farms.krushisanjivini.model.AddImageUrls
import com.farms.krushisanjivini.model.Notes
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.utilities.CheckInternetConnection
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationListingBinding
    private var mApiService: ApiServiceInterface?= null
    private val noteList = ArrayList<Notes>()
    private var noteParsedList = ArrayList<Notes>()
    var languageCode:String? ="kn"
    var userLanguageID:Int = 1
    private var notesNewID:Int = 0
    private var notesOldID:Int = 0
    private val list: ArrayList<AddImageUrls>? =ArrayList<AddImageUrls>()
    private var listOfImages: ArrayList<AddImageUrls>? =ArrayList<AddImageUrls>()

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
        binding = ActivityNotificationListingBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        languageCode=SharedPreferencesHelper.invoke(this).getSelectedLanguage()
        if(SharedPreferencesHelper.invoke(this).getSelectedLanguage().equals("kn")){
            setTitle("ತುರ್ತುಪರಿಸ್ಥಿಯ ಮಾಹಿತಿ")
            userLanguageID =2
        }else {
            setTitle("Notifications")
            userLanguageID =1
        }
        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            val message = bundle.getString("LanguageCode") // 1
            languageCode=message
        }
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(ApiServiceInterface::class.java)
        //getPlotList()
        val networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
        if(networkStatus) {
           getNotesList()
        }else{
            CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),this)
        }
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE)

    }


    private fun getNotesList(){

        if(SharedPreferencesHelper.invoke(this).getNewNoteID()!=null){
            notesNewID = SharedPreferencesHelper.invoke(this).getNewNoteID()!!
        }else
            notesNewID=0

        if(SharedPreferencesHelper.invoke(this).getOldNoteID()!=null){
            notesOldID = SharedPreferencesHelper.invoke(this).getOldNoteID()!!
        }else
            notesOldID=0
        println("Notes Old ID= $notesOldID")
        binding.progressbar.visibility = View.VISIBLE
            mApiService!!.getLastNotesBylangID(userLanguageID,notesNewID,notesOldID).enqueue(object:
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.code()==200||response.code()==201|| response.code()==202){
                        val stringResponse = response.body()?.string()
                        println("$stringResponse")
                        noteParsedList= stringResponse?.let { parseJson(it) }!!
                        //SharedPreferencesHelper.invoke(this@NotificationListActivity).saveOldNoteID(notesNewID)
                        if(noteParsedList.size>0){
                            binding.plotsListRecyclerView.adapter = NotificationListAdapter(noteParsedList,languageCode)
                            //loadImages(noteParsedList)
                        }
                    }else if(response.code()==401){
                        CheckInternetConnection.showSessionTimeOutDialog("User login session expired!!.",this@NotificationListActivity)
                    }else{

                    }
                    binding.progressbar.visibility = View.GONE
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println(t.message)
                }
            })

    }

    private fun parseJson(jsonResponse: String):ArrayList<Notes>{
        val jsonArray = JSONArray(jsonResponse)
        for (i in 0..jsonArray!!.length() - 1) {
            val jsonObj = jsonArray.optJSONObject(i)
            val transId =jsonObj.optInt("TransId")
            val noteId =jsonObj.optInt("NoteId")
            val langId =jsonObj.optInt("LangId")
            val noteText =jsonObj.optString("NoteText")
            val imageUrl =jsonObj.optString("ImageUrl")
            noteList.add(Notes(transId,noteId,langId,noteText,imageUrl))

        }

        return noteList
    }


}