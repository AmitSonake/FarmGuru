package com.farms.krushisanjivini.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.farms.krushisanjivini.databinding.HomeItemListingBinding
import com.farms.krushisanjivini.model.HomeMenuItems
import com.farms.krushisanjivini.schedule.PlotListingActivity
import com.farms.krushisanjivini.ui.mydiary.DiaryPlotListingActivity
import com.farms.krushisanjivini.ui.myplots.MyPlotActivity
import com.farms.krushisanjivini.ui.myplots.MyPlotListingActivity
import com.farms.krushisanjivini.ui.questions.QuestionHomeActivity


class HomeItemsAdapter(private val homeItemsList: MutableList<HomeMenuItems>,private val homeImageList: ArrayList<Int>)
    : RecyclerView.Adapter<HomeItemsAdapter.ViewHolder>() {
    //private var mApiService: ApiServiceInterface?= null
   // private lateinit var activityContext :Context
    inner class ViewHolder(val binding: HomeItemListingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val binding =HomeItemListingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the current animal
      //  holder.binding.imageView.setImageResource(homeItemsList[position].homeMenuImage)
        holder.binding.titleLabel.text=homeItemsList[position].homeMenuTitle
        holder.binding.imageView.setImageResource(homeImageList[position])

        holder.binding.cardViewItem.setOnClickListener {
            if(position==0){
                val intent = Intent(Intent(it.context, PlotListingActivity::class.java))
                ContextCompat.startActivity(it.context, intent, null)
            }else if(position==1){
                val intent = Intent(Intent(it.context, QuestionHomeActivity::class.java))
                ContextCompat.startActivity(it.context, intent, null)
            }else if(position==2){
                val intent = Intent(Intent(it.context, MyPlotListingActivity::class.java))
                ContextCompat.startActivity(it.context, intent, null)
            }else if(position==3){
                val intent = Intent(Intent(it.context, DiaryPlotListingActivity::class.java))
                ContextCompat.startActivity(it.context, intent, null)
            }else if(position==4){
                val intent = Intent(Intent(it.context, MyPlotActivity::class.java))
                ContextCompat.startActivity(it.context, intent, null)
            }/*else if(position==7){
                val intent = Intent(Intent(it.context, WeatherActivity::class.java))
                ContextCompat.startActivity(it.context, intent, null)
            }*/else{

            }

        }
       /* holder.binding.titleValue.text = plotsList[position].FarmerName
        holder.binding.subTitleValue.text = plotsList[position].FarmerAddress
        holder.binding.buttonMakePayment.setOnClickListener {

        }*/

    }


    override fun getItemCount(): Int {
        // number of items in the data set held by the adapter
        return homeItemsList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}