package com.farms.krushisanjivini.adapters.spinners

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.R
import com.farms.krushisanjivini.databinding.PlotListingItemBinding
import com.farms.krushisanjivini.model.PlotListing
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.schedule.MyScheduleActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PlotListAdapter(private val plotsList: MutableList<PlotListing>, private var userLanguage:String?)
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
        if(userLanguage.equals("kn")){
            holder.binding.farmIdLabel.text="ತೋಟದ ಐಡಿ:"
            holder.binding.titleLabel.text="ರೈತರ ಹೆಸರು:"
            holder.binding.subTitleLabel.text="ರೈತರ ವಿಳಾಸ:"
            holder.binding.cropNameLabel.text="ಬೆಳೆಯ ಹೆಸರು:"
            holder.binding.cropVarietyLabel.text="ಬೆಳೆ ವೈವಿಧ್ಯ:"
            holder.binding.cropSeasonLabel.text="ಬೆಳೆಯ ಕಾಲ:"
            holder.binding.viewSchedule.text="ವೇಳಾಪಟ್ಟಿಯನ್ನು ವೀಕ್ಷಿಸಿ"

            holder.binding.cropDistanceLabel.text="ಬೆಳೆಯ ಅಂತರ:"
            holder.binding.plotAgeLabel.text="ಸಸ್ಯದ ವಯಸ್ಸು:"
            holder.binding.plotAreaLabel.text="ಒಟ್ಟು ಪ್ರದೇಶ (ಎಕರೆಗಳು):"
            holder.binding.paymentStatusLabel.text="ಹಣ ಪಾವತಿಯ ಸ್ಥಿತಿ:"
        }
        holder.binding.titleValue.text = plotsList[position].FarmerName
        holder.binding.farmIdValue.text = plotsList[position].PlotID
        holder.binding.subTitleValue.text = plotsList[position].FarmerAddress

        holder.binding.cropDistanceValue.text = plotsList[position].CropDistance
        holder.binding.plotAgeValue.text = plotsList[position].PlotAge.toString()
        holder.binding.plotAreaValue.text = plotsList[position].PlotArea.toString()
        if(!plotsList[position].IsPaid){
            if(userLanguage.equals("kn")){
            holder.binding.paymentStatusValue.text = "Pending"
            }else{
                holder.binding.paymentStatusValue.text = "Pending"
            }
            holder.binding.paymentStatusValue.setTextColor(ContextCompat.getColor(holder.binding.root.context,
                R.color.red_color))
        }
        else{
            if(userLanguage.equals("kn")){
                holder.binding.paymentStatusValue.text = "Done"
            }else{
                holder.binding.paymentStatusValue.text = "Done"
            }

            holder.binding.paymentStatusValue.setTextColor(ContextCompat.getColor(holder.binding.root.context,
                R.color.green_color))
        }


        /*if(plotsList[position].CropId.equals(1)){
            if(SharedPreferencesHelper.invoke(holder.binding.root.context).getSelectedLanguage().equals("EN"))
            holder.binding.cropVarietyValue.text = "Grapes"
                else
                    holder.binding.cropVarietyValue.text = "ದ್ರಾಕ್ಷಿಗಳು"
        }else if(plotsList[position].CropId.equals(2)){
                if(SharedPreferencesHelper.invoke(holder.binding.root.context).getSelectedLanguage().equals("EN"))
                    holder.binding.cropVarietyValue.text = "Pomogranate"
                    else
                    holder.binding.cropVarietyValue.text = "ದಾಳಿಂಬೆ"


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
        }*/
        holder.binding.farmIdValue.text=plotsList[position].PlotID
        holder.binding.cropNameValue.text=plotsList[position].Crop
        holder.binding.cropVarietyValue.text =plotsList[position].CropVariety
        holder.binding.cropSeasonValue.text = plotsList[position].CropSeason
      /*  if(plotsList[position].IsPaid==true){
            holder.binding.editFarm.visibility= View.VISIBLE
        }else{
            holder.binding.editFarm.visibility= View.GONE
        }*/
     /* holder.binding.deleteFarm.setOnClickListener {
          var removePlotId =plotsList[position].RegId
            deleteUserPlot(removePlotId,it.context)
      }*/

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