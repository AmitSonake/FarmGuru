package com.farms.krushisanjivini.ui.videos

import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebChromeClient
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.databinding.VideoViewBinding
import com.farms.krushisanjivini.network.ApiServiceInterface


class VideoPreviewActivity : AppCompatActivity() {
    private lateinit var binding:VideoViewBinding
    private var mApiService: ApiServiceInterface?= null
    private var userLanguageID:Int=1
    private var videoUrl:String?= " "
    private var languageCode:String?= "kn"


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
        binding = VideoViewBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
       // getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setHomeButtonEnabled(true)
        languageCode=SharedPreferencesHelper.invoke(this).getSelectedLanguage()
        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            //userLanguageID = bundle.getInt("userLanguageID") // 1
            videoUrl =intent.extras?.getString("videoUrl")
            println("Youtube Url= $videoUrl")
        }
        var videoId=videoUrl
        val frameVideo ="<iframe width=\"100%\" height=\"100%\" src=\"https://www" +
                ".youtube.com/embed/$videoId\" frameborder=\"0\" allowfullscreen></iframe>"
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webChromeClient = object : WebChromeClient() {
        }
        if (videoId != null) {
            binding.webView.loadData(frameVideo, "text/html", "utf-8")
        }
        if(SharedPreferencesHelper.invoke(this).getSelectedLanguage().equals("kn")){
            userLanguageID =2
            supportActionBar?.title="ಸೇವೆ ಒದಗಿಸುವವರು"
        }else {
            userLanguageID =1
        }
    }

}