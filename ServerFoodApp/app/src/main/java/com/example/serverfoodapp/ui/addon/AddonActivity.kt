package com.example.serverfoodapp.ui.addon

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.serverfoodapp.R
import com.example.serverfoodapp.adapter.AddonAdapter
import com.example.serverfoodapp.callback.AddonCallback
import com.example.serverfoodapp.callback.MyItemTouch
import com.example.serverfoodapp.callback.UpdateCallback
import com.example.serverfoodapp.common.Comm
import com.example.serverfoodapp.model.Addon
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddonActivity : AppCompatActivity() , AddonCallback {
    private var toolbar: Toolbar? =null

    private var addonNameEdt: EditText? = null
    private var addonPriceEdt: EditText? = null
    private var saveBtn: Button? = null
    private var createBtn: Button? = null
    private var recyclerView: RecyclerView? = null
    private val addons: ArrayList<Addon> = ArrayList<Addon>()
    private var addonAdapter: AddonAdapter? = null
    private var progressBar: ProgressBar? = null
//    private val addonResult: ArrayList<Addon> = ArrayList<Addon>()
    private val searchView: SearchView? = null
    private val searchEdit: EditText? = null
    private var myItemTouch: MyItemTouch? = null

    private lateinit var addonViewModel : AddonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addon)
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (getSupportActionBar() != null){
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()!!.setDisplayShowHomeEnabled(true);
        }

        addonViewModel=ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(AddonViewModel::class.java)

        addonNameEdt = findViewById(R.id.foodNameEdt)
        addonPriceEdt = findViewById(R.id.foodPriceEdt)
        saveBtn = findViewById(R.id.savaeBtn)
        createBtn = findViewById(R.id.createBtn)
        recyclerView = findViewById(R.id.addonRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        recyclerView!!.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        recyclerView!!.setHasFixedSize(true)
        addonAdapter = AddonAdapter(addons, this)
        recyclerView!!.setAdapter(addonAdapter)

        addonViewModel.mutableLiveData.observe(this,
            Observer<List<Addon>> {addonList->
//                addonAdapter!!.setAddonList(addons)
                addons.clear()
                addons.addAll(addonList)
                progressBar!!.setVisibility(View.GONE)
                addonAdapter!!.notifyDataSetChanged()
                initMyItemTouch()
                Toast.makeText(this@AddonActivity, "addon Changed", Toast.LENGTH_SHORT).show()
            })


//        getData()



        createBtn!!.setOnClickListener(View.OnClickListener { view: View? ->
            val addon =
                Addon(addonNameEdt!!.getText().toString(), addonPriceEdt!!.getText().toString().toDouble())
            addons.add(addon)

//            /*
//                you can delete below feature if you want , it is depending on requirement .
//                 */if (!addonResult.isEmpty()) {
//            addonResult.add(addon)
//        }
            val hashMap =
                HashMap<String, Any>()
            hashMap["addon"] = addons
            FirebaseDatabase.getInstance().reference.child("Category")
                .child(Comm.selectedMenu!!.key!!).child("foods").child(Comm.selectedFood!!.key!!).updateChildren(
                    hashMap
                ) { error: DatabaseError?, ref: DatabaseReference? ->
                    Log.d("pos", addons.size.toString() + "")
                    Log.d("pos", "added successfully")
                    addonAdapter!!.notifyDataSetChanged()
                }
        })

    }

    private fun initMyItemTouch() {
            if (myItemTouch != null) {
                myItemTouch!!.deattachSwipe()
            }
            myItemTouch = object : MyItemTouch(this@AddonActivity, recyclerView) {
                override fun instantiateUnderlayButton(
                    viewHolder: RecyclerView.ViewHolder?,
                    underlayButtons: MutableList<UnderlayButton>?
                ) {
                    underlayButtons!!.add(
                        UnderlayButton(
                            "Delete",
                            0,
                            Color.RED,
                            object : UnderlayButtonClickListener {
                                override fun onClick(pos: Int) {
                                    deleteAddonItem(pos)
                                }
                            })
                    )
                }

            }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // todo: goto back activity from here
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    private fun deleteAddonItem(pos: Int) {
        addons.removeAt(pos)
        addonAdapter!!.notifyItemRemoved(pos)
        addonAdapter!!.notifyItemRangeChanged(pos, addons.size)

       updateFirebase(object : UpdateCallback {
           override fun onSuccess(task: Task<Void>) {
               Log.d("pos", pos.toString() + "")
               Log.d("pos", "delete successfully")

                   addonNameEdt!!.setText("")
                   addonNameEdt!!.hint = "Enter Addon Name"
                   addonPriceEdt!!.setText("")
                   addonPriceEdt!!.hint = "Enter Price"
                   saveBtn!!.isEnabled = false
           }

           override fun onFailed(exception: Exception) {
               TODO("Not yet implemented")
           }
       })

    }

    private fun updateFirebase(updateCallback: UpdateCallback) {
        val hashMap =
            HashMap<String, Any>()
        hashMap["addon"] = addons
        FirebaseDatabase.getInstance().reference.child("Category")
            .child(Comm.selectedMenu!!.key!!).child("foods").child(Comm.selectedFood!!.key!!)
            .updateChildren(
                hashMap
            ) .addOnCompleteListener {
                updateCallback.onSuccess(task = it)
            }.addOnFailureListener {
                updateCallback.onFailed(it)
            }

    }

    override fun AddonItem(addon: Addon?, pos: Int) {
        addonNameEdt!!.setText(addon!!.name)
        addonPriceEdt!!.setText(addon!!.price.toString())
        saveBtn!!.isEnabled = true
        saveBtn!!.setOnClickListener {
            if (addonNameEdt!!.text.toString().isNullOrEmpty() || addonPriceEdt!!.text.toString().isNullOrEmpty()){
                Toast.makeText(this@AddonActivity,"Please Enter All Information",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            addon.name=addonNameEdt!!.text.toString()
            addon.price=addonPriceEdt!!.text.toString().toDouble()
            addons[pos] = addon

            updateFirebase(object : UpdateCallback {
                override fun onSuccess(task: Task<Void>) {
                    addonAdapter!!.notifyItemChanged(pos)
                }

                override fun onFailed(exception: Exception) {

                }
            })
        }
    }

    override fun deleteAdodon(addon: Addon?, pos: Int, view: View?) {
        deleteAddonItem(pos)
    }

}