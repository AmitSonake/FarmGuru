package com.farms.krushisanjivini.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.farms.krushisanjivini.databinding.WebinarListingItemBinding
import com.farms.krushisanjivini.model.Webinars
import com.farms.krushisanjivini.network.ApiServiceInterface
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class WebinarListAdapter(private val webinarList: MutableList<Webinars>, private var userLanguage:String?)
    : RecyclerView.Adapter<WebinarListAdapter.ViewHolder>() {
    private var mApiService: ApiServiceInterface?= null
   inner class ViewHolder(val binding: WebinarListingItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val binding =WebinarListingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the current animal
        /*if(userLanguage.equals("kn")){
            holder.binding.farmIdLabel.text="ತೋಟದ ಐಡಿ:"
            holder.binding.titleLabel.text="ರೈತರ ಹೆಸರು:"
            holder.binding.subTitleLabel.text="ರೈತರ ವಿಳಾಸ:"
            holder.binding.cropNameLabel.text="ಬೆಳೆಯ ಹೆಸರು:"

        }*/
        //holder.binding.webinarTitleLabel.text =webinarList[position].WebinarTitle
       // holder.binding.webLinkLabel.text = webinarList[position].WebinarLink
       // holder.binding.webinarDateLabel.text = webinarList[position].WebinarDateTime

        val date= webinarList[position].WebinarDateTime
        val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val formattedDate = formatter.format(parser.parse(date))




      //  val  dateFormat = date?.split("T")[0]
        val time= date!!?.split("T")[1]

        holder.binding.webinarDateLabel.text = formattedDate
        holder.binding.webinarTime.text =""+ getTimeWithAMPMFromTime(time)
        val url:String=webinarList[position].WebinarLink


        holder.binding.webLinkLabel.setOnClickListener {
            url.asUri()?.openInBrowser(it.context)
        }

    }


    fun String?.asUri(): Uri? {
        return try {
            Uri.parse(this)
        } catch (e: Exception) {
            null
        }
    }
    fun Uri?.openInBrowser(context: Context) {
        this ?: return // Do nothing if uri is null

        val browserIntent = Intent(Intent.ACTION_VIEW, this)
        ContextCompat.startActivity(context, browserIntent, null)
    }
    private fun launchZoomUrl() {

    }
    override fun getItemCount(): Int {
        // number of items in the data set held by the adapter
        return webinarList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun getTimeWithAMPMFromTime(dt: String?): String? {
        return try {
            val inFormat = SimpleDateFormat("HH:mm:ss")
            val date: Date = inFormat.parse(dt)
            val outFormat = SimpleDateFormat("hh:mm a")
            outFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }
}