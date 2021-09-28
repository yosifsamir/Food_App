package com.example.myfoodapp.ui.cart

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.developer.kalert.KAlertDialog
import com.example.myfoodapp.R
import com.example.myfoodapp.adapter.CartAdapter
import com.example.myfoodapp.callback.DeleteCartCallback
import com.example.myfoodapp.callback.MyItemTouch
import com.example.myfoodapp.callback.OrderCallback
import com.example.myfoodapp.common.Comm
import com.example.myfoodapp.database.MyDatabase
import com.example.myfoodapp.model.Cart
import com.example.myfoodapp.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.observers.ConsumerSingleObserver
import io.reactivex.schedulers.Schedulers
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class CartActivity : AppCompatActivity(), CartAdapter.CartCallback, View.OnClickListener {
    private  var itemTouchItem: ItemTouchHelper?=null
    private val listOfCarts: List<Cart>?=ArrayList()
    private var compisteDisposable:CompositeDisposable= CompositeDisposable()
    private lateinit var myDatabase:MyDatabase
    private lateinit var recyclerView: RecyclerView
    private  var cartAdapter: CartAdapter ?=null
    private lateinit var cartViewModel: CartViewModel
    private lateinit var totalTxt: TextView
    private var recyclerViewState: Parcelable? = null

    private lateinit var placeOrderBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        myDatabase= MyDatabase.getDatabase(this)
        recyclerView=findViewById(R.id.recycler_cart)
        totalTxt=findViewById(R.id.totalTxt)
        placeOrderBtn=findViewById(R.id.placeOrderBtn)

        placeOrderBtn.setOnClickListener(this)

        cartViewModel=ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(CartViewModel::class.java)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        var mutableLiveData=cartViewModel.mutableLiveDataCarts

        mutableLiveData.observe(this, object : Observer<List<Cart>> {
            override fun onChanged(listOfCarts2: List<Cart>?) {
//                Toast.makeText(this@CartActivity,"inside observer ${listOfCarts!!.size}",Toast.LENGTH_LONG).show()
                cartAdapter= CartAdapter(this@CartActivity,listOfCarts2)
                recyclerView.adapter=cartAdapter
//                (this@CartActivity.listOfCarts!! as ArrayList).clear()
//                (this@CartActivity.listOfCarts!! as ArrayList).addAll(listOfCarts2!!)
//                if (cartAdapter==null){
//                    cartAdapter= CartAdapter(this@CartActivity,listOfCarts)
//                    recyclerView.adapter=cartAdapter
//                }
//                else
//                    cartAdapter!!.notifyDataSetChanged()

//                mutableLiveData.removeObservers(this@CartActivity)

            }
        })

        createItemTouchDelete()

        calCulateTotalPrice()

    }

    override fun onStart() {
        super.onStart()
    }

    private fun createItemTouchDelete() {


        val swipeHelper: MyItemTouch = object : MyItemTouch(this@CartActivity, recyclerView) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>?
            ) {
                underlayButtons!!.add(UnderlayButton(
                    "Delete",
                    0,
                    Color.parseColor("#FF3C30"),
                    object : UnderlayButtonClickListener {
                        override fun onClick(pos: Int) {
                            var progressDialog=ProgressDialog(this@CartActivity)
//                            progressDialog.create()
                            progressDialog.show()
                            Toast.makeText(this@CartActivity,"Position item ${pos}",Toast.LENGTH_LONG).show()
                            var cart=cartAdapter!!.getCartItem(pos)
                            cartViewModel.deleteCartItem(cart,
                                object : DeleteCartCallback {
                                    override fun success(onNext: Int) {
                                        Toast.makeText(this@CartActivity,"success ${onNext}",Toast.LENGTH_LONG).show()
//                                        (listOfCarts as ArrayList).removeAt(onNext)
                                        progressDialog.dismiss()
                                        cartAdapter!!.notifyItemRemoved(pos) // re-code this again there are some error .
//                                        cartAdapter!!.notifyDataSetChanged()

                                        cartViewModel.getTotalPrice()
                                    }

                                    override fun failed(onError: Throwable) {
                                        Toast.makeText(this@CartActivity,"${onError.message}",Toast.LENGTH_LONG).show()
                                    }
                                })
                        }
                    }
                ))
            }

        }
        itemTouchItem=ItemTouchHelper(swipeHelper)
        itemTouchItem!!.attachToRecyclerView(recyclerView)

    }


    @SuppressLint("CheckResult")
    override fun numberOfQuantity(cart: Cart , currentPosition:Int) {
//        compisteDisposable.add()

        recyclerViewState=recyclerView.layoutManager!!.onSaveInstanceState()
        var position=(recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        Log.d("fff",position.toString())
        compisteDisposable.add(myDatabase.cartDao().updateCartItem(cart).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({onSuccess->
                recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)
                recyclerView.scrollToPosition(position)
                cartViewModel.getTotalPrice()
            }
                ,{onError->
                    Toast.makeText(this@CartActivity,"${onError}",Toast.LENGTH_LONG).show()
                }))
    }

    private fun calCulateTotalPrice() {
        cartViewModel.mutableLiveDataTotal.observe(this, object : Observer<Double> {
            override fun onChanged(total: Double?) {
                if (total==null){
                    totalTxt.setText("0 $")
                    return
                }
                totalTxt.setText(DecimalFormat("##.00").format(total)+"$")


            }
        })

    }




    override fun onStop() {
        super.onStop()
        compisteDisposable.clear()
//        cartViewModel.onStop()
//        itemTouchItem!!.attachToRecyclerView(null)
    }

    override fun onClick(view: View?) {
        if(view!!.id==R.id.placeOrderBtn){
            placeOrder()
        }
    }

    private fun placeOrder() {
        var alertDialog=AlertDialog.Builder(this)
        var view=LayoutInflater.from(this).inflate(R.layout.place_order_layout,null)
        var addressEditText=view.findViewById<EditText>(R.id.addressEdt)
        var homeAddressRadioBtn=view.findViewById<RadioButton>(R.id.homeAddressRadioBtn)
        var otherAddressRadioBtn=view.findViewById<RadioButton>(R.id.otherAddressRadioBtn)
        var shipAddressRadioBtn=view.findViewById<RadioButton>(R.id.shipAddressRadioBtn)
        var cashOnDelivery=view.findViewById<RadioButton>(R.id.cashDeliveryRadioBtn)


        alertDialog.setPositiveButton("OK", object : DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                if (homeAddressRadioBtn.isChecked){
                    if (addressEditText.text.toString().length>0)
                        paymentCOD(addressEditText.text.toString())
                }
            }
        })

        alertDialog.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                dialogInterface!!.dismiss()
            }
        })

        alertDialog.setView(view)
        var builder=alertDialog.create()
        builder.show()

        homeAddressRadioBtn.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(compoundButton: CompoundButton?, boolean: Boolean) {
                if (boolean==true){
                    addressEditText.isEnabled=true
                    addressEditText.setText("new Damietta Alhy 2")
                    builder.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled=true

                }
            }
        })
        homeAddressRadioBtn.isChecked=true

        otherAddressRadioBtn.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(compoundButton: CompoundButton?, boolean: Boolean) {
                if (boolean==true){
                    addressEditText.setText("Will Enable as soonn as possible")
                    addressEditText.isEnabled=false

                    builder.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled=false

                }
            }
        })

        shipAddressRadioBtn.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(compoundButton: CompoundButton?, boolean: Boolean) {
                if (boolean==true){

                }
            }
        })

        cashOnDelivery.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(compoundButton: CompoundButton?, boolean: Boolean) {
                if (boolean==true){

                }
            }
        })

    }

    private fun paymentCOD(toString: String) {
//        compisteDisposable.clear()
        var myCompositeDisposable=CompositeDisposable()
        myCompositeDisposable.add(MyDatabase.getDatabase(this@CartActivity)
            .cartDao()
            .getAllCart(FirebaseAuth.getInstance().currentUser.uid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({onNext->
//                insertOrderToFirebase(onNext)
//
//            },{onError->
//                Toast.makeText(this@CartActivity,onError.message,Toast.LENGTH_LONG).show()
//            }))
            .subscribe(object : Consumer<List<Cart>> {
                override fun accept(t: List<Cart>?) {
                    Toast.makeText(this@CartActivity,"accept is called",Toast.LENGTH_LONG).show()
                    insertOrderToFirebase(t)
                    myCompositeDisposable.dispose()
                }
            }))

    }

    private fun insertOrderToFirebase(listOfCarts: List<Cart>?) {
        if (listOfCarts!!.isEmpty()){
            showHintDialog()
            return
        }
        else{
            compisteDisposable.add(MyDatabase.getDatabase(this@CartActivity)
                .cartDao()
                .sumItemCart(FirebaseAuth.getInstance().currentUser.uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({onNext->



                    var order=Order()
                    order.userId=FirebaseAuth.getInstance().currentUser.uid
                    order.userName=Comm.currentUser!!.name
                    order.userPhone=Comm.currentUser!!.phone
                    order.shippingAddress=null // check this later and all the below later.
                    order.comment=null
                    order.lat=null
                    order.lng=null
                    order.cartItemList=listOfCarts
                    order.totalPayment=onNext
                    order.discount=0
                    order.finalPayment=onNext // check this later , make it variable and modify it later on again .
                    order.cod=true
                    order.transactionId="Cash On Delivery"
                    order.orderStatus=0
                    order.createdAt=Date().time

                    cartViewModel.insertOrderToFirebase(order, object : OrderCallback {
                        override fun success(isSuccessful: Boolean) {
                            if (isSuccessful){
                                deleteAllItemInCart()
                            }
                        }

                        override fun failed(onError: Throwable) {
                            Toast.makeText(this@CartActivity,onError.message,Toast.LENGTH_LONG).show()
                        }
                    })

                },{onError->
                    Toast.makeText(this@CartActivity,onError.message,Toast.LENGTH_LONG).show()
                }))
        }
    }

    private fun showHintDialog() {
        val pDialog = KAlertDialog(this, KAlertDialog.WARNING_TYPE)
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"))
        pDialog.setTitleText("Please Choose Any Food \nThat You Want To Buy.")
        pDialog.setCancelable(false)
        pDialog.show()
    }

    private fun deleteAllItemInCart() {
        compisteDisposable.add(MyDatabase.getDatabase(this)
            .cartDao()
            .clearDbCartForUserId(FirebaseAuth.getInstance().currentUser.uid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({onNext->
                val pDialog = KAlertDialog(this, KAlertDialog.SUCCESS_TYPE)
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"))
                pDialog.setTitleText("Done")
                pDialog.setCancelable(false)
                pDialog.show()
                totalTxt.setText("0 $")
                return@subscribe
            },{onError->
                Toast.makeText(this@CartActivity,onError.message,Toast.LENGTH_LONG).show()
            }))
    }
}