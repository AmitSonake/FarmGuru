package com.farms.krushisanjivini.ui.dealers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.farms.krushisanjivini.databinding.ActivityDealerSearchBinding

class DealerSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDealerSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDealerSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.payButton.setOnClickListener {
           // makePayment()
        }

    }

    /*private fun makePayment(){
        val checkout = Checkout()

        // set your id as below
        checkout.setKeyID("rzp_test_cPeCKKKGVVndiE")

        // set image
        checkout.setImage(R.drawable.grape_icon)

        // initialize json object
        val jObject = JSONObject()
        try {
            // to put name
            jObject.put("name", "xyz Product")

            // put description
            jObject.put("description", "Test payment")

            // to set theme color
            jObject.put("theme.color", "white")

            // put the currency
            jObject.put("currency", "INR")

            // put amount
            jObject.put("amount", "1.0")

            // put mobile number
            jObject.put("prefill.contact", "9890400545")

            // put email
            jObject.put("prefill.email", "sanjivinikrushi@gmail.com")

            // open razorpay to checkout activity
            checkout.open(this@DealerSearchActivity, jObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(s: String?) {
        Toast.makeText(this, "Payment is successful : " + s, Toast.LENGTH_SHORT).show();
    }

    override fun onPaymentError(s: Int, p1: String?) {
        Toast.makeText(this, "Payment is failed due to error : " + s, Toast.LENGTH_SHORT).show();
    }*/

}