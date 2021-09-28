package com.example.serverfoodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.serverfoodapp.R
import com.example.serverfoodapp.callback.AddonCallback
import com.example.serverfoodapp.model.Addon

class AddonAdapter(addonList: List<Addon>, context: Context) : RecyclerView.Adapter<AddonAdapter.AddonViewHolder>() {
    private var addonList: List<Addon>
    private val context: Context
    private val addonCallback: AddonCallback
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddonViewHolder {
        return AddonViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.addon_layout, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: AddonViewHolder,
        position: Int
    ) {

        holder.addonNameTxt.setText(addonList[position].name)
        holder.addonPriceTxt.setText(addonList[position].price.toString() + " $")
        holder.itemView.setOnClickListener {
            addonCallback.AddonItem(
                addonList[position],
                position
            )
        }
        holder.deleteAddonImg.setOnClickListener { view ->
            addonCallback.deleteAdodon(
                addonList[position],
                position,
                view
            )
        }
    }

    fun getAddonList(): List<Addon> {
        return addonList
    }

    fun setAddonList(addonList: List<Addon>) {
        this.addonList = addonList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return addonList.size
    }

    inner class AddonViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var addonNameTxt: TextView
        var addonPriceTxt: TextView
        var deleteAddonImg: ImageView

        init {
            addonNameTxt = itemView.findViewById(R.id.addonNameTxt)
            addonPriceTxt = itemView.findViewById(R.id.addonPriceTxt)
            deleteAddonImg = itemView.findViewById(R.id.deleteAddonImg)
        }
    }

    init {
        this.addonList = addonList
        this.context = context
        addonCallback = context as AddonCallback
    }
}
