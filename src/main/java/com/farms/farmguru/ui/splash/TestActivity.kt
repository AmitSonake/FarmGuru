package com.farms.farmguru.ui.splash

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.farms.farmguru.ComplaintRegistrationActivity
import com.farms.farmguru.R
import com.farms.farmguru.databinding.ActivityDashboardBinding
import com.farms.farmguru.databinding.ActivityRegisterCropsBinding
import com.farms.farmguru.databinding.ActivitySubscriptionBinding
import com.farms.farmguru.databinding.ActivityUserPlotRegistrationBinding
import com.farms.farmguru.ui.myplots.MyPlotActivity
import com.farms.farmguru.ui.myplots.MyPlotListingActivity
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class TestActivity : AppCompatActivity(), PaymentResultListener {
    //private lateinit var binding: ActivityRegisterCropsBinding
    //private lateinit var binding: ActivitySubscriptionBinding
    //private lateinit var binding: ActivityUserPlotRegistrationBinding
    private lateinit var binding: ActivityDashboardBinding
    var cal = Calendar.getInstance()
    val TAG:String = TestActivity::class.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Checkout.preload(applicationContext)
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
            /*val intent = Intent(this, MyPlotActivity::class.java)
            startActivity(intent)*/
             makePayment()

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

    override fun onPaymentSuccess(s: String?) {
        Toast.makeText(this, "Payment is successful  : " + s, Toast.LENGTH_SHORT).show();
    }

    override fun onPaymentError(s: Int, p1: String?) {
        Toast.makeText(this, "Payment is failed due to error : " + s, Toast.LENGTH_SHORT).show();
    }

    private fun makePayment(){
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_cPeCKKKGVVndiE")
        try {
            val options = JSONObject()
            options.put("name","Razorpay Corp")
            options.put("description","Demoing Charges")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
            options.put("amount","199")//pass amount in currency subunits
            val prefill = JSONObject()
            prefill.put("email","abc.gv@example.com")
            prefill.put("contact","9876543220")
            options.put("prefill",prefill)
            checkout.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}