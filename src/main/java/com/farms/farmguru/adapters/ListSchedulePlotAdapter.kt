package com.farms.farmguru.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.farmguru.adapters.SelectCropAdapter
import com.farms.farmguru.databinding.PlotListingItemBinding
import com.farms.farmguru.databinding.PlotSchedulelistingItemBinding
import com.farms.farmguru.model.PlotListing
import com.farms.farmguru.network.ApiClient
import com.farms.farmguru.network.ApiServiceInterface
import com.farms.farmguru.schedule.MyScheduleActivity
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListSchedulePlotAdapter(private val plotsList: MutableList<PlotListing>,private var userLanguage:String?)
    : RecyclerView.Adapter<ListSchedulePlotAdapter.ViewHolder>(), PaymentResultListener {
    private var mApiService: ApiServiceInterface?= null
    private lateinit var activityContext :Context
    inner class ViewHolder(val binding: PlotSchedulelistingItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val binding =PlotSchedulelistingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the current animal
        if(userLanguage.equals("kn")){
            holder.binding.titleLabel.text="ರೈತರ ಹೆಸರು:"
            holder.binding.subTitleLabel.text="ರೈತರ ವಿಳಾಸ:"
            holder.binding.cropVarietyLabel.text="ಬೆಳೆ ಹೆಸರು:"
            holder.binding.cropSeasonLabel.text="ಬೆಳೆ ಋತು:"
            holder.binding.viewSchedule.text="ವೇಳಾಪಟ್ಟಿಯನ್ನು ವೀಕ್ಷಿಸಿ"
            holder.binding.buttonMakePayment.text="ಪಾವತಿ ಮಾಡಿ"
        }
        holder.binding.titleValue.text = plotsList[position].FarmerName
        holder.binding.subTitleValue.text = plotsList[position].FarmerAddress
        if(plotsList[position].CropId.equals(1)){
            holder.binding.cropVarietyValue.text = "Grapes"
        }else if(plotsList[position].CropId.equals(2)){
            holder.binding.cropVarietyValue.text = "Pomogrenate"
        }else if(plotsList[position].CropId.equals(3)){
            holder.binding.cropVarietyValue.text = "Capcicum"
        }else if(plotsList[position].CropId.equals(4)){
            holder.binding.cropVarietyValue.text = "Tomato"
        }else if(plotsList[position].CropId.equals(5)){
            holder.binding.cropVarietyValue.text = "Brinjal"
        }else if(plotsList[position].CropId.equals(6)){
            holder.binding.cropVarietyValue.text = "Chili"
        }else if(plotsList[position].CropId.equals(7)){
            holder.binding.cropVarietyValue.text = "Onion"
        }else{
            holder.binding.cropVarietyValue.text = "Soon..."
        }
        holder.binding.cropSeasonValue.text = plotsList[position].CropSeason
        if(plotsList[position].IsPaid==true){
            holder.binding.editFarm.visibility= View.VISIBLE
        }else{
            holder.binding.editFarm.visibility= View.GONE
        }
        holder.binding.deleteFarm.visibility=View.GONE
      holder.binding.deleteFarm.setOnClickListener {
          var removePlotId =plotsList[position].RegId
            deleteUserPlot(removePlotId,it.context)
      }

        holder.binding.viewSchedule.setOnClickListener {
            var removePlotId =plotsList[position].RegId
            val intent = Intent(it.context, MyScheduleActivity::class.java)
            intent.putExtra("RegID",removePlotId)
            ContextCompat.startActivity(it.context, intent, null)
           // overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        holder.binding.buttonMakePayment.setOnClickListener {
           makePayment(it.context as Activity)
            // overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }

    }


    override fun getItemCount(): Int {
        // number of items in the data set held by the adapter
        return plotsList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun deleteUserPlot(plotId:Int,context:Context){
        //binding.progressbar.visibility = View.VISIBLE
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(context).getToken())!!.create(ApiServiceInterface::class.java)

        mApiService!!.deletePlot(plotId).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                }
                //binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })

    }

    override fun onPaymentSuccess(s: String?) {
        Toast.makeText(activityContext, "Payment is successful  : " + s, Toast.LENGTH_SHORT).show();

    }

    override fun onPaymentError(s: Int, p1: String?) {
        Toast.makeText(activityContext, "Payment is failed due to error : " + s, Toast.LENGTH_SHORT).show();
    }


    private fun makePayment(context:Activity){
        val checkout = Checkout()
        activityContext = context
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
            checkout.open(context,options)
        }catch (e: Exception){
            Toast.makeText(context,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}