package com.farms.krushisanjivini.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farms.krushisanjivini.databinding.ServiceProviderListingItemBinding
import com.farms.krushisanjivini.model.ServiceProviders
import com.farms.krushisanjivini.network.ApiServiceInterface


class VideosListAdapter(private val providersList: MutableList<ServiceProviders>, private var userLanguage:String?)
    : RecyclerView.Adapter<VideosListAdapter.ViewHolder>() {
    private var mApiService: ApiServiceInterface?= null
   inner class ViewHolder(val binding: ServiceProviderListingItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =ServiceProviderListingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.providerTitleLabel.text =providersList[position].ServiceProviderName
       // holder.binding.villageLabel.text = providersList[position].Village
        holder.binding.phonenoLabel.text = providersList[position].PhNo+" | "+""+providersList[position].Village
        holder.binding.serviceLabel.text = providersList[position].Service

       /* val imageUrl:String="https://webapi.krushisanjivini.com/images/service$cropid.png"
        val options = RequestOptions()
            .placeholder(R.drawable.test)
            .error(R.drawable.test)
        Glide.with(holder.binding.root)
            .setDefaultRequestOptions(options)
            .load(imageUrl)
            .into(holder.binding.logoImageview)*/
    }

    override fun getItemCount(): Int {
        return providersList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}