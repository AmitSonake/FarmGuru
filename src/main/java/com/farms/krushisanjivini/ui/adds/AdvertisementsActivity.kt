package com.farms.krushisanjivini.ui.adds

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.R
import com.farms.krushisanjivini.adapters.KonwledgeBankImageListAdapter
import com.farms.krushisanjivini.databinding.ActivityAdvertisementBinding
import com.farms.krushisanjivini.model.AddImageUrls
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.utilities.CheckInternetConnection
import com.google.firebase.storage.FirebaseStorage


class AdvertisementsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdvertisementBinding
    private var mApiService: ApiServiceInterface?= null
    var context: Context? = null
    var languageCode:String? ="kn"
    private var userLanguageID:Int=1
    private val list: ArrayList<AddImageUrls>? =ArrayList<AddImageUrls>()

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
        binding = ActivityAdvertisementBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val view =binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(ApiServiceInterface::class.java)
        languageCode=SharedPreferencesHelper.invoke(this).getSelectedLanguage()
        if(SharedPreferencesHelper.invoke(this).getSelectedLanguage().equals("kn")){
            userLanguageID =2
            supportActionBar?.title="ಜಾಹೀರಾತು"
        }else {
            userLanguageID =1
        }
        GridLayoutManager(
            this, // context
            1,
            // span count
            RecyclerView.VERTICAL, // orientation
            false // reverse layout
        ).apply {
            // specify the layout manager for recycler view
            binding.knowledgePostListRecyclerView.layoutManager=this
        }

        val networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
        if(networkStatus){
            //getKnowledgePostList(binding.knowledgePostListRecyclerView)
                loadImages()
            //listStoredImages()
        }else{
            CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),this)
            finish()
        }




    }

    private fun loadImages(){
        val listRef = FirebaseStorage.getInstance().reference.child("adds")
        listRef.listAll().addOnSuccessListener { listResult ->
            for (file in listResult.items) {
                val url= file.downloadUrl
                System.out.println("Image url = $url")
                file.downloadUrl.addOnSuccessListener { uri ->
                    list?.add(AddImageUrls(uri.toString()))
                    Log.e("Itemvalue", uri.toString())
                }.addOnSuccessListener {
                    binding.knowledgePostListRecyclerView.adapter = list?.let { it1 ->
                        KonwledgeBankImageListAdapter(
                            it1
                        )
                    }
                    //progressBar.setVisibility(View.GONE)
                }
            }
        }
    }
}