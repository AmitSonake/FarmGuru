package com.farms.farmguru.adapters.spinners

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.farms.farmguru.R
import com.farms.farmguru.databinding.RowItemCropVarietyBinding
import com.farms.farmguru.model.CropVariety

class CropVarietySpinnerAdapter(val context: Context, var dataSource: List<CropVariety>) : BaseAdapter() {
    private lateinit var binding: RowItemCropVarietyBinding
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
           // val binding = RowItemCropVarietyBinding.inflate(LayoutInflater.from(parent?.context),parent,false)
            //return ViewHolder(binding)
            view = inflater.inflate(R.layout.row_item_crop_variety, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.label.text= dataSource.get(position).CropVarietyName
        //vh.binding.label.text = dataSource.get(position).CropVarietyName

       // val id = contex.t.resources.getIdentifier(dataSource.get(position).url, "drawable", context.packageName)
      //  vh.img.setBackgroundResource(id)

        return view
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(vh:View) {
        val label: TextView
        //val img: ImageView

        init {
            label = vh.findViewById(R.id.text) as TextView
                    // img = findViewById(R.id.img) as ImageView
        }
    }

}