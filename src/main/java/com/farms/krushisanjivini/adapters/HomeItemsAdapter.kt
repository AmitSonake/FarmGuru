package com.farms.krushisanjivini.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.databinding.HomeItemListingBinding
import com.farms.krushisanjivini.model.HomeMenuItems
import com.farms.krushisanjivini.schedule.PlotListingActivity
import com.farms.krushisanjivini.ui.adds.AdvertisementsActivity
import com.farms.krushisanjivini.ui.agreeinput.AgreeInputDealerActivity
import com.farms.krushisanjivini.ui.knowledgebank.KnowledgeBankActivity
import com.farms.krushisanjivini.ui.mydiary.DiaryPlotListingActivity
import com.farms.krushisanjivini.ui.myplots.MyPlotActivity
import com.farms.krushisanjivini.ui.myplots.MyPlotListingActivity
import com.farms.krushisanjivini.ui.questions.QuestionHomeActivity
import com.farms.krushisanjivini.ui.serviceproviders.ServiceProvidersListingActivity
import com.farms.krushisanjivini.ui.webinar.WebinarListingActivity
import com.farms.krushisanjivini.utilities.util.showSubscriptionAlertReminder


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
                if(SharedPreferencesHelper.invoke(it.context).getIsUserPlotActive() == true) {
                    val intent = Intent(Intent(it.context, PlotListingActivity::class.java))
                    ContextCompat.startActivity(it.context, intent, null)
                }else{
                    showMessage(it.context)
                }
            }else if(position==1){
                if(SharedPreferencesHelper.invoke(it.context).getIsUserPlotActive() == true) {
                    val intent = Intent(Intent(it.context, QuestionHomeActivity::class.java))
                    ContextCompat.startActivity(it.context, intent, null)
                }else{
                    showMessage(it.context)
                }

            }else if(position==2){
                val intent = Intent(Intent(it.context, MyPlotListingActivity::class.java))
                ContextCompat.startActivity(it.context, intent, null)
            }else if(position==3){
                if(SharedPreferencesHelper.invoke(it.context).getIsUserPlotActive() == true) {
                    val intent = Intent(Intent(it.context, DiaryPlotListingActivity::class.java))
                    ContextCompat.startActivity(it.context, intent, null)
                }else{
                    showMessage(it.context)
                }

            }else if(position==4){
                val intent = Intent(Intent(it.context, MyPlotActivity::class.java))
                ContextCompat.startActivity(it.context, intent, null)
            }else if(position==5){
                if(SharedPreferencesHelper.invoke(it.context).getIsUserPlotActive() == true) {
                    val intent = Intent(Intent(it.context, KnowledgeBankActivity::class.java))
                    ContextCompat.startActivity(it.context, intent, null)
                }else{
                    showMessage(it.context)
                }

            }else if(position==8){
                if(SharedPreferencesHelper.invoke(it.context).getIsUserPlotActive() == true) {
                    val intent = Intent(Intent(it.context, WebinarListingActivity::class.java))
                    ContextCompat.startActivity(it.context, intent, null)
                }else{
                    showMessage(it.context)
                }
            }else if(position==9){
                if(SharedPreferencesHelper.invoke(it.context).getIsUserPlotActive() == true) {
                    val intent = Intent(Intent(it.context, ServiceProvidersListingActivity::class.java))
                    ContextCompat.startActivity(it.context, intent, null)
                }else{
                    showMessage(it.context)
                }
            }else if(position==6){
                if(SharedPreferencesHelper.invoke(it.context).getIsUserPlotActive() == true) {
                    val intent = Intent(Intent(it.context, AgreeInputDealerActivity::class.java))
                    ContextCompat.startActivity(it.context, intent, null)
                }else{
                    showMessage(it.context)
                }
            }else if(position==7){
                if(SharedPreferencesHelper.invoke(it.context).getIsUserPlotActive() == true) {
                    val intent = Intent(Intent(it.context, AdvertisementsActivity::class.java))
                    ContextCompat.startActivity(it.context, intent, null)
                }else{
                    showMessage(it.context)
                }
            }else if(position==10){
                showMessageSoon(it.context)
            }else if(position==11){
                showMessageSoon(it.context)
            }else{

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

    private fun showMessage(context: Context){
        if(SharedPreferencesHelper.invoke(context).getSelectedLanguage().equals("kn"))
            showSubscriptionAlertReminder(context,"ಕ್ಷಮಿಸಿ, ನಿಮ್ಮ ತೋಟದ ಐಡಿ ಸಕ್ರಿಯ ಇಲ್ಲದ ಕಾರಣ ನಿವು ಕಂಪನಿಯ ಸಹಾಯವಾಣಿಗೆ ಕರೆ ಮಾಡಿಕೊಳ್ಳಿ -(+91 8352317645)")
        else
            showSubscriptionAlertReminder(context,"Sorry,your plot is not active contact company customer care number(+91 8352317645)  ")
    }

    private fun showMessageSoon(context: Context){
        if(SharedPreferencesHelper.invoke(context).getSelectedLanguage().equals("kn"))
            showSubscriptionAlertReminder(context,"ಕ್ಷಮಿಸಿ, ಈ ವೈಶಿಷ್ಟ್ಯವು ಶೀಘ್ರದಲ್ಲೇ ಬರಲಿದೆ ")
        else
            showSubscriptionAlertReminder(context,"Sorry, this feature is coming soon ")
    }



}