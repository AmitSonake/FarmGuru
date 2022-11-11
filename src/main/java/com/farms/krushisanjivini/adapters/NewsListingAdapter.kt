package com.farms.krushisanjivini.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farms.krushisanjivini.databinding.RowMainArticleAdapterBinding
import com.farms.krushisanjivini.model.NewsData


class NewsListingAdapter(private val newsList: MutableList<NewsData>)
    : RecyclerView.Adapter<NewsListingAdapter.ViewHolder>() {

   inner class ViewHolder(val binding: RowMainArticleAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val binding =RowMainArticleAdapterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
       /* val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_view,parent,false)

        // return the view holder
        return ViewHolder(view)*/

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the current animal
        holder.binding.articleAdapterTvTitle.text = newsList.get(position).title
        holder.binding.articleAdapterTvDescription.text = newsList.get(position).description
    }


    override fun getItemCount(): Int {
        // number of items in the data set held by the adapter
        return newsList.size
    }


   // inner class ViewHolder(val binding: CustomViewBinding) : RecyclerView.ViewHolder(binding.root)


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }
}