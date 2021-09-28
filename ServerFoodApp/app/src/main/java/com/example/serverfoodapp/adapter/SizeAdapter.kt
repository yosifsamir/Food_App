package com.example.serverfoodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.serverfoodapp.R
import com.example.serverfoodapp.callback.SizeCallback

import com.example.serverfoodapp.model.Size

class SizeAdapter(sizeList: List<Size>, context: Context) : RecyclerView.Adapter<SizeAdapter.SizeViewHolder>() {
    private var sizeList: List<Size>
    private val context: Context
    private val sizeCallback: SizeCallback
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SizeViewHolder {
        return SizeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.size_layout, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: SizeViewHolder,
        position: Int
    ) {

        holder.sizeNameTxt.setText(sizeList[position].name)
        holder.sizePriceTxt.setText(sizeList[position].price.toString() + " $")
        holder.itemView.setOnClickListener {
            sizeCallback.sizeItem(
                sizeList[position],
                position
            )
        }
        holder.deleteSizeImg.setOnClickListener { view ->
            sizeCallback.deleteSize(
                sizeList[position],
                position,
                view
            )
        }
    }

    fun getAddonList(): List<Size> {
        return sizeList
    }

    fun setAddonList(addonList: List<Size>) {
        this.sizeList = sizeList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return sizeList.size
    }

    inner class SizeViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var sizeNameTxt: TextView
        var sizePriceTxt: TextView
        var deleteSizeImg: ImageView

        init {
            sizeNameTxt = itemView.findViewById(R.id.sizeNameTxt)
            sizePriceTxt = itemView.findViewById(R.id.sizePriceTxt)
            deleteSizeImg = itemView.findViewById(R.id.sizeAddonImg)
        }
    }

    init {
        this.sizeList = sizeList
        this.context = context
        sizeCallback = context as SizeCallback
    }
}
