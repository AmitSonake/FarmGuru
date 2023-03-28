package com.farms.krushisanjivini.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farms.krushisanjivini.databinding.AgreeInputDealersListingItemBinding
import com.farms.krushisanjivini.model.Dealers
import com.farms.krushisanjivini.network.ApiServiceInterface


class AgreeInputDealersListAdapter(private val dealersList: ArrayList<Dealers>?, private var userLanguage:String?)
    : RecyclerView.Adapter<AgreeInputDealersListAdapter.ViewHolder>() {
    private var mApiService: ApiServiceInterface?= null
   inner class ViewHolder(val binding: AgreeInputDealersListingItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =AgreeInputDealersListingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.dealerTitleLabel.text = dealersList?.get(position)?.DealerName
        holder.binding.addressLabel.text = dealersList!![position].Address
        holder.binding.districtLabel.text = dealersList[position].City +", "+
                dealersList[position].Taluka+", "+dealersList[position].District
       /* holder.binding.villageLabel.text = dealersList[position].Taluka
        holder.binding.villageLabel.text = dealersList[position].District*/
        holder.binding.phonenoLabel.text = dealersList[position].PhNo

    }

    override fun getItemCount(): Int {
        return dealersList!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}