package com.farms.krushisanjivini.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farms.krushisanjivini.databinding.NotesListingItemBinding
import com.farms.krushisanjivini.model.Notes
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.utilities.util.getProgressDrawable
import com.farms.krushisanjivini.utilities.util.loadImage


class NotificationListAdapter(private val notesList: MutableList<Notes>, private var userLanguage:String?)
    : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {
    private var mApiService: ApiServiceInterface?= null
   inner class ViewHolder(val binding: NotesListingItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val binding =NotesListingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the current animal
        //.binding.notesWebViewTitles.settings.javaScriptEnabled=true
        val imageUrl=notesList[position].ImageUrl
            holder.binding.imageView.loadImage(imageUrl,
                getProgressDrawable(holder.binding.imageView.context)
            )

       val notificationTitle= notesList.get(position).NoteText.toString()
        if(notificationTitle.isNullOrEmpty())
        {
            holder.binding.notesTitle.text =""
        }else if(notificationTitle.equals("null")){
            holder.binding.notesTitle.text =""
        }else
        holder.binding.notesTitle.text = "${notesList.get(position).NoteText}"
       // holder.binding.notesWebViewTitles.loadDataWithBaseURL(null,notesList.get(position).NoteText.toString(),"text/html", "utf-8", null)
        //holder.binding.notesTitle.text = "${notesList.get(position).NoteId}. ${notesList.get(position).NoteText}"
    }


    override fun getItemCount(): Int {
        // number of items in the data set held by the adapter
        return notesList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}


