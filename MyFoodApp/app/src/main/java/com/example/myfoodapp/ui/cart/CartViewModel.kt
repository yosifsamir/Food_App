package com.example.myfoodapp.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myfoodapp.callback.DeleteCartCallback
import com.example.myfoodapp.callback.OrderCallback
import com.example.myfoodapp.common.Comm
import com.example.myfoodapp.database.MyDatabase
import com.example.myfoodapp.model.Cart
import com.example.myfoodapp.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CartViewModel(application: Application) : AndroidViewModel(application) {
    var mutableLiveDataTotal=MutableLiveData<Double>()
    var mutableLiveDataCarts=MutableLiveData<List<Cart>>()
    var application2=application

    private var compisteDisposable: CompositeDisposable = CompositeDisposable()
    init {

        compisteDisposable.add(MyDatabase.getDatabase(application2)
            .cartDao()
            .getAllCart(FirebaseAuth.getInstance().currentUser.uid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({onSuccess-> mutableLiveDataCarts.postValue(onSuccess)},{onError-> mutableLiveDataCarts.postValue(null)})
        )

       getTotalPrice()
    }

    fun getTotalPrice(){
        compisteDisposable.add(MyDatabase.getDatabase(application2)
            .cartDao()
            .sumItemCart(FirebaseAuth.getInstance().currentUser.uid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({onSuccess-> mutableLiveDataTotal.postValue(onSuccess)},{onError-> mutableLiveDataTotal.postValue(null)})
        )
    }

    fun deleteCartItem(cart: Cart, deleteCartCallback: DeleteCartCallback){
        compisteDisposable.add(MyDatabase.getDatabase(application2)
            .cartDao()
            .deleteCartItem(cart)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({onNext-> deleteCartCallback.success(onNext)},{onError-> deleteCartCallback.failed(onError) }))
    }

    fun onStop(){
        compisteDisposable.clear()
    }

    fun insertOrderToFirebase(order: Order,orderCallback: OrderCallback) {
        FirebaseDatabase.getInstance().reference.child("Order")
            .child(Comm.getTimeAndRandom()).setValue(order).addOnCompleteListener({
                orderCallback.success(it.isSuccessful)
            }).addOnFailureListener({
                orderCallback.failed(it)
            })
    }
}