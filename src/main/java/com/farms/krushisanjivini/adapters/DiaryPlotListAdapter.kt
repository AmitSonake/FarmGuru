package com.farms.krushisanjivini.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.ui.mydiary.MyDiaryScheduleActivity
import com.farms.krushisanjivini.databinding.DiaryPlotListingItemBinding
import com.farms.krushisanjivini.model.PlotListing
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.utilities.util.showSubscriptionAlertReminder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DiaryPlotListAdapter(private val plotsList: MutableList<PlotListing>, private var userLanguage:String?)
    : RecyclerView.Adapter<DiaryPlotListAdapter.ViewHolder>() {
    private var mApiService: ApiServiceInterface?= null
   inner class ViewHolder(val binding: DiaryPlotListingItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val binding =DiaryPlotListingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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
        }
        holder.binding.titleValue.text = plotsList[position].FarmerName
        holder.binding.subTitleValue.text = plotsList[position].FarmerAddress
       /* if(plotsList[position].CropId.equals(1)){
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
        holder.binding.cropVarietyValue.text =plotsList[position].Crop
            holder.binding.cropSeasonValue.text = plotsList[position].CropSeason
       /* if(plotsList[position].IsPaid==true){
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
            var isPaid :Boolean =plotsList[position].IsPaid
          //  var isTrial :Boolean =plotsList[position].IsTrial
            /*if(!isPaid && isTrial){
                val intent = Intent(it.context, MyDiaryScheduleActivity::class.java)
                intent.putExtra("RegID",removePlotId)
                intent.putExtra("isTrial",isTrial)
                ContextCompat.startActivity(it.context, intent, null)
            }else */
            if(!isPaid/* && !isTrial*/){
            if(userLanguage.equals("kn"))
               showSubscriptionAlertReminder(it.context,"ಕ್ಷಮಿಸಿ, ನಿಮ್ಮ ತೋಟದ ಐಡಿ ಸಕ್ರಿಯ ಇಲ್ಲದ ಕಾರಣ ನಿವು ಕಂಪನಿಯ ಸಹಾಯವಾಣಿಗೆ ಕರೆ ಮಾಡಿಕೊಳ್ಳಿ -(+91 8352317645)")
            else
               showSubscriptionAlertReminder(it.context,"Sorry,your plot is not active contact company customer care number(+91 8352317645)  ")
        }else{
                val intent = Intent(it.context, MyDiaryScheduleActivity::class.java)
                intent.putExtra("RegID",removePlotId)
                //intent.putExtra("isTrial",isTrial)
                ContextCompat.startActivity(it.context, intent, null)
            }

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