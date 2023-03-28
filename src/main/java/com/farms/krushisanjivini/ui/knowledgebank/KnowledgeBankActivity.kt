package com.farms.krushisanjivini.ui.knowledgebank

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.R
import com.farms.krushisanjivini.adapters.KonwledgeBankImageListAdapter
import com.farms.krushisanjivini.databinding.ActivityKnowledgePostBinding
import com.farms.krushisanjivini.model.AddImageUrls
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.utilities.CheckInternetConnection
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class KnowledgeBankActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKnowledgePostBinding
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
        binding = ActivityKnowledgePostBinding.inflate(layoutInflater)
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
            supportActionBar?.title="ಮಾಹಿತಿ ಬಂಡಾರ"
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


    val imageRef = Firebase.storage.reference
    private fun listStoredImages() = CoroutineScope(Dispatchers.IO).launch {
        try {
           /* val userImages = imageRef.child(uid).listAll().await()
            val publicImages = imageRef.child("knowledge_bank").listAll().await()

            // Loads private images from storage
            for (image in userImages.items) {
                val url = image.downloadUrl.await()
                userImageUrl.add(url.toString())

            }   // loads public images from storage
            for (publicImage in publicImages.items) {
                val publicUrl = publicImage.downloadUrl.await()
                userImageUrl.add(publicUrl.toString())
            }*/

           // val listRef = FirebaseStorage.getInstance().reference.child("images")
            FirebaseStorage.getInstance().reference.child("knowledge_bank").listAll().addOnSuccessListener { listResult ->
                for (file in listResult.items) {
                    file.downloadUrl.addOnSuccessListener { uri ->
                        list!!.add(AddImageUrls(uri.toString()))
                       // Log.e("Itemvalue", uri.toString())
                    }
                        //progressBar.setVisibility(View.GONE)

                }
            }
            withContext(Dispatchers.Main) {
               // if(list?.size!! >0){
               //     binding.knowledgePostListRecyclerView.adapter = KonwledgeBankImageListAdapter(list)
               // }
              //  binding.knowledgePostListRecyclerView.adapter?.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@KnowledgeBankActivity, "Error", Toast.LENGTH_SHORT).show()
                if(list?.size!! >0){
                    binding.knowledgePostListRecyclerView.adapter = KonwledgeBankImageListAdapter(list)
                }
               // binding.knowledgePostListRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun loadImages(){
        val listRef = FirebaseStorage.getInstance().reference.child("knowledge_bank")
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