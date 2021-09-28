package com.example.myfoodapp.ui.Foods

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.myfoodapp.R
import com.example.myfoodapp.common.Comm
import com.example.myfoodapp.fragment.CommentsBottomNavigation
import com.example.myfoodapp.model.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ServerValue
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.CompletableObserver
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FoodDetailsActivity : AppCompatActivity() {

    private lateinit var txtFoodName: TextView
    private lateinit var txtPrice:TextView
    private lateinit var txtDesc:TextView
    private lateinit var imgFood: ImageView
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var elegantNumberButton: ElegantNumberButton
    private lateinit var allCommentsTxt:TextView
    private lateinit var addCommentsTxt:TextView
    private lateinit var addOnRadioGroup:RadioGroup
    private lateinit var sizeRadioGroup:RadioGroup

    private lateinit var foodDetailsViewModel: FoodDetailsViewModel
    var currentAddon:Addon?=null
    var currentSize:Size?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_food_details)
        supportActionBar!!.hide()
        foodDetailsViewModel=ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(FoodDetailsViewModel::class.java)
        initViews()
        foodDetailsViewModel.foodMutableLiveData.observe(this, object : Observer<Food> {
            @SuppressLint("RestrictedApi")
            override fun onChanged(food: Food?) {
                if (food==null)
                    return
                txtFoodName.text=food!!.name
                txtDesc.text=food.description
                txtPrice.text= food.price.toString()
                Picasso.get().load(food.image).into(imgFood)
//                txtPrice.text=(elegantNumberButton.number.toDouble() * food.price!!).toString()
                addAddOnLayoutAndListener(food)
                addSizeLayoutAndListener(food)

                initComments()

                elegantNumberButton.setOnClickListener(object : ElegantNumberButton.OnClickListener {
                    override fun onClick(view: View?) {
                        txtPrice.text=((food.price!! * elegantNumberButton.number.toDouble()) + (currentAddon!!.price!! +currentSize!!.price!!) * elegantNumberButton.number.toDouble()).toString()

                    }
                })
            }
        })

        floatingActionButton.setOnClickListener({
            var cart=Cart(
                Comm.selectedFood!!.id!!,Comm.selectedFood!!.name,
                Comm.selectedFood!!.image,
                Comm.selectedFood!!.price,elegantNumberButton.number.toInt(),"Empty",0.0, Gson().toJson(currentAddon),Gson().toJson(currentSize),FirebaseAuth.getInstance().currentUser.uid)

            foodDetailsViewModel.insertIntoDatabase(cart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        finish()
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(this@FoodDetailsActivity,"${e.message}",Toast.LENGTH_LONG).show()

                    }
                })
        })
    }

    private fun initComments() {
        allCommentsTxt.setOnClickListener({
//                    Toast.makeText(this@FoodDetailsActivity,"You Clicked me",Toast.LENGTH_LONG).show()
            createBottomNavigation()
        })

        addCommentsTxt.setOnClickListener({
            createDialog()
        })
    }

    private fun addSizeLayoutAndListener(food: Food) {
        if (food.size==null)
            return
        for (size in food.size!!){
            var radioButton=RadioButton(this@FoodDetailsActivity)
            radioButton.layoutParams=RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,RadioGroup.LayoutParams.WRAP_CONTENT)
            radioButton.text=size.name
            radioButton.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(compoundButton: CompoundButton?, state: Boolean) {
                    if(state){
                        currentSize=size!!
                        if(currentAddon!=null) {
                            txtPrice.text =
                                ((food.price!! * elegantNumberButton.number.toDouble()) + currentSize!!.price!! + currentAddon!!.price!!).toString()
                        }else{
                            txtPrice.text =
                                ((food.price!! * elegantNumberButton.number.toDouble()) + currentSize!!.price!!).toString()
                        }

                    }

                }
            })
            sizeRadioGroup.addView(radioButton)
        }
        if (sizeRadioGroup.childCount >0){
            var radioButton=sizeRadioGroup.getChildAt(0) as RadioButton
            currentSize= food.size!![0]
            radioButton.isChecked=true
        }

    }

    private fun addAddOnLayoutAndListener(food: Food) {
        //                currentAddon:Addon?=null
        if (food.addon==null)
            return
        for (addon in food.addon!!){
            var radioButton=RadioButton(this@FoodDetailsActivity)
            radioButton.layoutParams=RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,RadioGroup.LayoutParams.WRAP_CONTENT)
            radioButton.text=addon.name
            radioButton.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(compoundButton: CompoundButton?, state: Boolean) {
                    if (state){
                        currentAddon=addon!!
                        if(currentSize!=null){
                            txtPrice.text=((food.price!! * elegantNumberButton.number.toDouble()) + addon.price!!+ currentSize!!.price!!).toString()
                        }else
                            txtPrice.text=((food.price!! * elegantNumberButton.number.toDouble()) + addon.price!!).toString()
                    }

                }
            })
            addOnRadioGroup.addView(radioButton)
        }
        if (addOnRadioGroup.childCount >0){
            var radioButton=addOnRadioGroup.getChildAt(0) as RadioButton
            currentAddon= food.addon!![0]
            radioButton.isChecked=true
        }



    }

    @SuppressLint("RestrictedApi")
    private fun createDialog() {
        var alertDialog=AlertDialog.Builder(this@FoodDetailsActivity)
        val layoutParams=ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        var editText=EditText(this@FoodDetailsActivity)
        editText.layoutParams=layoutParams
        editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setSingleLine(false)
        editText.setLines(5);
        editText.setMaxLines(5);
        editText.setGravity(Gravity.LEFT or  Gravity.TOP)
//                    editText.setPadding(4,0,0,0)
        alertDialog.setTitle("Enter Your Comment")
        alertDialog.setView(editText,12,12,0,0);

        alertDialog.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                if (editText.text.toString().isEmpty()  || editText.text.trim().length == 0){
                    return
                }
                var comment=editText.text.toString()
                val serverTimeStamp: HashMap<String, Any> = HashMap()
                serverTimeStamp.put("timeStamp", ServerValue.TIMESTAMP)

                var commentModel=Comments(comment,Comm.currentUser!!.name,FirebaseAuth.getInstance().currentUser.uid,serverTimeStamp)
                foodDetailsViewModel.insertComment(commentModel)
                //////// add boolean to check the data is correctly sent or not and then you can dismiss or show error message
                foodDetailsViewModel.isSuccessFully.observe(this@FoodDetailsActivity, object : Observer<Boolean> {
                    override fun onChanged(t: Boolean?) {
                        if (t==true){
//                            dialogInterface!!.dismiss()
                            createBottomNavigation()
                            Toast.makeText(this@FoodDetailsActivity,"The Comment is sent Successfully",Toast.LENGTH_LONG).show()
                        }
                        else{
                            dialogInterface!!.dismiss()
                            Toast.makeText(this@FoodDetailsActivity,"Something wrong",Toast.LENGTH_LONG).show()

                        }

                    }
                })



            }
        })
        alertDialog.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                dialogInterface!!.dismiss()

            }
        })

        val alertDialog2=alertDialog.create()
        alertDialog2.show()

        alertDialog2.setOnDismissListener({
            foodDetailsViewModel.isSuccessFully.removeObservers(this)
        })
    }

    private fun createBottomNavigation() {
        CommentsBottomNavigation().show(supportFragmentManager,"commentBottom")
    }

    private fun initViews() {
        txtFoodName=findViewById(R.id.food_name_details)
        txtPrice=findViewById(R.id.food_price_details)
        txtDesc=findViewById(R.id.description_details)
        imgFood=findViewById(R.id.food_image_details)
        elegantNumberButton=findViewById(R.id.number_counter_2)
        allCommentsTxt=findViewById(R.id.showAllCommentsTxt)
        addCommentsTxt=findViewById(R.id.addCommentsTxt)
        addOnRadioGroup=findViewById(R.id.addOnRadioGroup)
        sizeRadioGroup=findViewById(R.id.sizeRadioGroup)

        floatingActionButton=findViewById(R.id.my_cart)
    }

}