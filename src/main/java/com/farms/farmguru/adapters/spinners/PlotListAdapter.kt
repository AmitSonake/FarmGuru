package com.farms.farmguru.adapters.spinners

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.farmguru.adapters.SelectCropAdapter
import com.farms.farmguru.databinding.PlotListingItemBinding
import com.farms.farmguru.model.PlotListing
import com.farms.farmguru.network.ApiClient
import com.farms.farmguru.network.ApiServiceInterface
import com.farms.farmguru.schedule.MyScheduleActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PlotListAdapter(private val plotsList: MutableList<PlotListing>)
    : RecyclerView.Adapter<PlotListAdapter.ViewHolder>() {
    private var mApiService: ApiServiceInterface?= null
   inner class ViewHolder(val binding: PlotListingItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val binding =PlotListingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the current animal
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
}