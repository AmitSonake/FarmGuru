package com.farms.krushisanjivini.ui.videos

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.R
import com.farms.krushisanjivini.adapters.VideosListAdapter
import com.farms.krushisanjivini.databinding.ActivityVideoplayBinding
import com.farms.krushisanjivini.model.AddYoutubeUrls
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.utilities.CheckInternetConnection
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response





class VideoListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoplayBinding
    private var mApiService: ApiServiceInterface?= null
    var context: Context? = null
    var languageCode:String? ="kn"
    private var userLanguageID:Int=1
    private var pageIndex:Int=0
    private var pageSize:Int=10000
    private var cropId:String? = null

    private val videoUrlList = ArrayList<AddYoutubeUrls>()
    private var parsedVideoUrlList = ArrayList<AddYoutubeUrls>()

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
        binding = ActivityVideoplayBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val view =binding.root
        setContentView(view)
        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            cropId = bundle.getString("cropId")
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(ApiServiceInterface::class.java)
        languageCode=SharedPreferencesHelper.invoke(this).getSelectedLanguage()
        if(SharedPreferencesHelper.invoke(this).getSelectedLanguage().equals("kn")){
            userLanguageID =2
            supportActionBar?.title="ವಿಡಿಯೋಗಳು ನಿಮಗಾಗಿ"
        }else {
            userLanguageID =1
        }
        val networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
        if(networkStatus){
           getVideosListing(pageIndex,pageSize)
        }else{
            CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),this)
            finish()
        }

        // adding on scroll change listener method for our nested scroll view.
        // adding on scroll change listener method for our nested scroll view.
       /* binding.idNestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
               // pageIndex++
                pageSize=pageSize+2
               // binding.progressbar.setVisibility(View.VISIBLE)
                if(networkStatus){
                    getVideosListing(pageIndex,pageSize)
                }else{
                    CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),this)
                   // finish()
                }
            }
        })*/
    }

    private fun getVideosListing(pageIndex:Int,pageSize:Int){
        var cropID:Int = Integer.parseInt(cropId)
        if (pageIndex > pageSize) {
            Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.progressbar.visibility = View.VISIBLE
        mApiService!!.getVideoUrlsByLangIdPage(userLanguageID,cropID, pageIndex,pageSize,"Search_Data").enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    parsedVideoUrlList= stringResponse?.let { parseJson(it.toString()) }!!
                    if(parsedVideoUrlList.size>0){
                        binding.recyclerView.adapter = VideosListAdapter(parsedVideoUrlList,userLanguageID.toString(),userLanguageID)
                    }else {
                        if(userLanguageID==2){
                            binding.noScheduleLabel.text="ಯಾವುದೇ ಡೇಟಾ ಲಭ್ಯವಿಲ್ಲ"
                        }else{
                            binding.noScheduleLabel.text="There is no any data available"
                        }
                        binding.noScheduleLabel.visibility = View.VISIBLE
                    }

                }else if(response.code()==401){
                    CheckInternetConnection.showSessionTimeOutDialog(
                        "User login session expired!!.",this@VideoListActivity)
                }else{

                }
                binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })
    }

    private fun parseJson(jsonResponse: String):ArrayList<AddYoutubeUrls>{
        val jsonArray = JSONArray(jsonResponse)
        for (i in 0..jsonArray!!.length() - 1) {
            val jsonObj = jsonArray.optJSONObject(i)
            val transId =jsonObj.optInt("TransId")
            val videoUrlId =jsonObj.optInt("VideoUrlId")
            val videoUrlText =jsonObj.optString("VideoUrlText")
            val thumbnailUrl =jsonObj.optString("ThumbnailUrl")
            val langId =jsonObj.optInt("LangId")
            val title =jsonObj.optString("Title")
            val description =jsonObj.optString("Description")
            val totalCount =jsonObj.optInt("AllRecCount")
            //pageSize=totalCount

            println("Url Text=$videoUrlText")
            videoUrlList.add(
                AddYoutubeUrls(transId,videoUrlId,videoUrlText,
                    thumbnailUrl,langId,title,description,totalCount)
            )
        }
        return videoUrlList
    }


}