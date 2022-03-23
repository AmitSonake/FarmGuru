package com.farms.farmguru.ui.splash

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import com.farms.farmguru.ComplaintRegistrationActivity
import com.farms.farmguru.databinding.ActivityDashboardBinding
import com.farms.farmguru.databinding.ActivityRegisterCropsBinding
import com.farms.farmguru.databinding.ActivitySubscriptionBinding
import com.farms.farmguru.databinding.ActivityUserPlotRegistrationBinding
import com.farms.farmguru.ui.myplots.MyPlotActivity
import com.farms.farmguru.ui.myplots.MyPlotListingActivity
import java.text.SimpleDateFormat
import java.util.*

class TestActivity : AppCompatActivity() {
    //private lateinit var binding: ActivityRegisterCropsBinding
    //private lateinit var binding: ActivitySubscriptionBinding
    //private lateinit var binding: ActivityUserPlotRegistrationBinding
    private lateinit var binding: ActivityDashboardBinding
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }
         binding.myPlotDetailButton.setOnClickListener {
             val intent = Intent(this,MyPlotListingActivity::class.java)
             startActivity(intent)
         }

         binding.registerAnotherPlotButton.setOnClickListener {
            val intent = Intent(this, MyPlotActivity::class.java)
            startActivity(intent)
         }

         binding.askQuestionButton.setOnClickListener {
            //val intent = Intent(this, ComplaintRegistrationActivity::class.java)
           // startActivity(intent)
         }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        var result= sdf.format(cal.time)
       // binding.cropPruningDate.setText(result)
    }

}