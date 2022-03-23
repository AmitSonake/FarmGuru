package com.farms.farmguru.ui.language

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.farms.farmguru.MainActivity
import com.farms.farmguru.databinding.ActivityLanguageSelectionBinding
import com.farms.farmguru.databinding.ActivityLanguagesBinding
import com.farms.farmguru.network.ApiServiceInterface
import com.farms.farmguru.ui.myplots.MyPlotActivity
import com.farms.farmguru.ui.myplots.MyPlotListingActivity
import com.farms.farmguru.ui.splash.TestActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LanguageSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguagesBinding
    private var mApiService: ApiServiceInterface?= null
    var context: Context? = null
    var langaugeCode="EN"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguagesBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)

        binding.submitButton.setOnClickListener {
            context=it.context
            com.farms.farmguru.utilities.LocaleHelper.setLocale(context!!, "kn")
           // val intent = Intent(it.context,MainActivity::class.java)

            val intent = Intent(it.context,MyPlotActivity::class.java)
            intent.putExtra("LanguageCode",langaugeCode)
            startActivity(intent)
            finish()

           // getPlotList()
        }
        binding.buttonEnglish.setOnClickListener {
            binding.buttonEnglish.setBackgroundColor(Color.parseColor("#2C590F"))
            binding.buttonEnglish.setTextColor(Color.parseColor("#FFFFFF"))
            binding.buttonKannada.setBackgroundColor(Color.parseColor("#AAAAAA"))
            binding.submitButton.setBackgroundColor(Color.parseColor("#2C590F"))
            binding.submitButton.setTextColor(Color.parseColor("#FFFFFF"))
            langaugeCode= "EN"
        }
        binding.buttonKannada.setOnClickListener {
            langaugeCode= "kn"
            binding.buttonKannada.setBackgroundColor(Color.parseColor("#2C590F"))
            binding.buttonKannada.setTextColor(Color.parseColor("#FFFFFF"))
            binding.buttonEnglish.setBackgroundColor(Color.parseColor("#AAAAAA"))
            binding.submitButton.setBackgroundColor(Color.parseColor("#2C590F"))
            binding.submitButton.setTextColor(Color.parseColor("#FFFFFF"))
        }

       /* binding.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected
            var selectedId = binding.radioGroup.checkedRadioButtonId
            val radio:RadioButton = group.findViewById(selectedId)
            Log.e("selectedtext-->",radio.text.toString())
            langaugeCode= radio.text.toString()
        })*/
    }

    private fun getPlotList(){
        binding.progressbar.visibility = View.VISIBLE
        mApiService!!.getPlotRegistered().enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    if(stringResponse.isNullOrEmpty()){
                        navigateToActivity(1)
                    }else{
                        navigateToActivity(2)
                    }

                }
                binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })

    }

    private fun navigateToActivity(flag: Int){
        if(flag==1){
            val intent = Intent(this,MyPlotActivity::class.java)
            intent.putExtra("LanguageCode",langaugeCode)
            startActivity(intent)
        }else {
            val intent = Intent(this, TestActivity::class.java)
            intent.putExtra("LanguageCode",langaugeCode)
            startActivity(intent)
        }
        finish()
    }
}