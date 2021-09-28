package com.example.serverfoodapp.ui.size

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.serverfoodapp.R
import com.example.serverfoodapp.adapter.AddonAdapter
import com.example.serverfoodapp.adapter.SizeAdapter
import com.example.serverfoodapp.callback.AddonCallback
import com.example.serverfoodapp.callback.MyItemTouch
import com.example.serverfoodapp.callback.SizeCallback
import com.example.serverfoodapp.callback.UpdateCallback
import com.example.serverfoodapp.common.Comm
import com.example.serverfoodapp.model.Addon
import com.example.serverfoodapp.model.Size
import com.example.serverfoodapp.ui.addon.AddonViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList
import java.util.HashMap

class SizeActivity : AppCompatActivity() , SizeCallback {

    private var toolbar: Toolbar? =null

    private var sizeNameEdt: EditText? = null
    private var sizePriceEdt: EditText? = null
    private var saveBtn: Button? = null
    private var createBtn: Button? = null
    private var recyclerView: RecyclerView? = null
    private val sizes: ArrayList<Size> = ArrayList<Size>()
    private var sizeAdapter: SizeAdapter? = null
    private var progressBar: ProgressBar? = null
    //    private val addonResult: ArrayList<Addon> = ArrayList<Addon>()
    private val searchView: SearchView? = null
    private val searchEdit: EditText? = null
    private var myItemTouch: MyItemTouch? = null

    private lateinit var sizeViewModel : SizeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_size)
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (getSupportActionBar() != null){
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()!!.setDisplayShowHomeEnabled(true);
        }

        sizeViewModel= ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(SizeViewModel::class.java)

        sizeNameEdt = findViewById(R.id.sizeNameEdt)
        sizePriceEdt = findViewById(R.id.sizePriceEdt)
        saveBtn = findViewById(R.id.saveBtn)
        createBtn = findViewById(R.id.createBtnSize)
        recyclerView = findViewById(R.id.sizeRecyclerView)
        progressBar = findViewById(R.id.progressBarSize)

        recyclerView!!.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        recyclerView!!.setHasFixedSize(true)
        sizeAdapter = SizeAdapter(sizes, this)
        recyclerView!!.setAdapter(sizeAdapter)

        sizeViewModel.mutableLiveData.observe(this,
            Observer<List<Size>> {sizeList->
                sizeAdapter!!.setAddonList(sizes)
                sizes.clear()
                sizes.addAll(sizeList)
                progressBar!!.setVisibility(View.GONE)
                sizeAdapter!!.notifyDataSetChanged()
                initMyItemTouch()
            })


//        getData()



        createBtn!!.setOnClickListener(View.OnClickListener { view: View? ->
            val size =
                Size(sizeNameEdt!!.getText().toString(), sizePriceEdt!!.getText().toString().toDouble())
            sizes.add(size)

//            /*
//                you can delete below feature if you want , it is depending on requirement .
//                 */if (!addonResult.isEmpty()) {
//            addonResult.add(addon)
//        }
            val hashMap =
                HashMap<String, Any>()
            hashMap["size"] = sizes
            FirebaseDatabase.getInstance().reference.child("Category")
                .child(Comm.selectedMenu!!.key!!).child("foods").child(Comm.selectedFood!!.key!!).updateChildren(
                    hashMap
                ) { error: DatabaseError?, ref: DatabaseReference? ->
                    Log.d("pos", sizes.size.toString() + "")
                    Log.d("pos", "added successfully")
                    sizeAdapter!!.notifyDataSetChanged()
                }
        })


    }

    private fun initMyItemTouch() {
        if (myItemTouch != null) {
            myItemTouch!!.deattachSwipe()
        }
        myItemTouch = object : MyItemTouch(this@SizeActivity, recyclerView) {
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
                                deleteSizeItem(pos)
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

    private fun deleteSizeItem(pos: Int) {
        sizes.removeAt(pos)
        sizeAdapter!!.notifyItemRemoved(pos)
        sizeAdapter!!.notifyItemRangeChanged(pos, sizes.size)

        updateFirebase(object : UpdateCallback {
            override fun onSuccess(task: Task<Void>) {
                Log.d("pos", pos.toString() + "")
                Log.d("pos", "delete successfully")

                sizeNameEdt!!.setText("")
                sizeNameEdt!!.hint = "Enter Addon Name"
                sizePriceEdt!!.setText("")
                sizePriceEdt!!.hint = "Enter Price"
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
        hashMap["size"] = sizes
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

    override fun sizeItem(size: Size?, pos: Int) {
        sizeNameEdt!!.setText(size!!.name)
        sizePriceEdt!!.setText(size!!.price.toString())
        saveBtn!!.isEnabled = true
        saveBtn!!.setOnClickListener {
            if (sizeNameEdt!!.text.toString().isNullOrEmpty() || sizePriceEdt!!.text.toString().isNullOrEmpty()){
                Toast.makeText(this@SizeActivity,"Please Enter All Information", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            size.name=sizeNameEdt!!.text.toString()
            size.price=sizePriceEdt!!.text.toString().toDouble()
            sizes[pos] = size

            updateFirebase(object : UpdateCallback {
                override fun onSuccess(task: Task<Void>) {
                    sizeAdapter!!.notifyItemChanged(pos)
                }

                override fun onFailed(exception: Exception) {

                }
            })
        }
    }

    override fun deleteSize(size: Size?, pos: Int, view: View?) {
        deleteSizeItem(pos)
    }
}