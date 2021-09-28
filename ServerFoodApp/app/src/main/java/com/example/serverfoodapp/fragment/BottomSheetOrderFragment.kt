package com.example.serverfoodapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.serverfoodapp.R
import com.example.serverfoodapp.common.Comm
import com.example.serverfoodapp.ui.order.OrdersViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetOrderFragment: BottomSheetDialogFragment {
    private lateinit var placedLinearLayout:LinearLayout
    private lateinit var shippingLinearLayout:LinearLayout
    private lateinit var shippedLinearLayout:LinearLayout
    private lateinit var cancelledLinearLayout:LinearLayout

    private val ordersViewModel:OrdersViewModel
    constructor(ordersViewModel: OrdersViewModel):super(){
        this.ordersViewModel=ordersViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.order_fragment_layout,container,false)
        placedLinearLayout=view.findViewById<LinearLayout>(R.id.placed_filter)
        shippingLinearLayout=view.findViewById<LinearLayout>(R.id.shipping_filter)
        shippedLinearLayout=view.findViewById<LinearLayout>(R.id.shipped_filter)
        cancelledLinearLayout=view.findViewById<LinearLayout>(R.id.cancelled_filter)

        initListener()

        return view
    }

    private fun initListener() {
        placedLinearLayout.setOnClickListener({
            ordersViewModel.getOrdersData(Comm.setStatusInfo(Comm.Status.PLACED))
            dismiss()
        })

        shippedLinearLayout.setOnClickListener({
            ordersViewModel.getOrdersData(Comm.setStatusInfo(Comm.Status.SHIPPED))
            dismiss()
        })

        shippingLinearLayout.setOnClickListener({
            ordersViewModel.getOrdersData(Comm.setStatusInfo(Comm.Status.SHIPPING))
            dismiss()
        })

        cancelledLinearLayout.setOnClickListener({
            ordersViewModel.getOrdersData(Comm.setStatusInfo(Comm.Status.CANCELD))
            dismiss()
        })
    }
}