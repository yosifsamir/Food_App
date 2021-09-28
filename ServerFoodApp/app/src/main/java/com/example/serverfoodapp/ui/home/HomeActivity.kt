package com.example.serverfoodapp.ui.home

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.example.serverfoodapp.R
import com.example.serverfoodapp.adapter.MenuAdapter
import com.example.serverfoodapp.callback.MyItemTouch
import com.example.serverfoodapp.common.Comm
import com.example.serverfoodapp.model.Menu
import com.example.serverfoodapp.ui.order.OrderActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_addon.*
import java.util.*
import kotlin.collections.HashMap

class HomeActivity : AppCompatActivity() {
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var adapter: MenuAdapter
    private var imgMenu: ImageView?=null
    private lateinit var layoutManager: LinearLayoutManager
    private var homeViewModel:HomeViewModel ? = null
    private var recyclerView: RecyclerView? =null
    private var itemTouchHelper:ItemTouchHelper ? =null
    private var listOfMenus= mutableListOf<Menu>()
    private var imgUri:Uri ?=null
    lateinit var homeToolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homeToolbar=findViewById(R.id.homeToolbar)
        setSupportActionBar(homeToolbar)
        homeToolbar.setTitle("Home")

        homeViewModel= ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(HomeViewModel::class.java)
        recyclerView=findViewById(R.id.recyclerMenu)
        layoutManager=LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager
        adapter= MenuAdapter(listOfMenus!!)
        recyclerView!!.adapter = adapter
        firebaseStorage=FirebaseStorage.getInstance()

        recyclerView!!.addItemDecoration(
            DividerItemDecoration(this@HomeActivity,
                DividerItemDecoration.VERTICAL)
        )
        homeViewModel!!.allMenu.observe(this, object : Observer<List<Menu>> {
            override fun onChanged(listOfMenu: List<Menu>?) {
//                Toast.makeText(this@HomeActivity,"inside observe",Toast.LENGTH_LONG).show()
                listOfMenus.clear()
                listOfMenus.addAll(listOfMenu!!)
                adapter.notifyDataSetChanged()
            }

        })
        createUpdateItem()


    }



    private fun createUpdateItem() {

        itemTouchHelper= ItemTouchHelper(object : MyItemTouch(this@HomeActivity,recyclerView!!) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>?
            ) {
                underlayButtons!!.add(UnderlayButton("UPDATE",0, Color.parseColor("#FF3C30"),
                    object : UnderlayButtonClickListener {
                        override fun onClick(pos: Int) {
                            Comm.selectedMenu=listOfMenus[pos]
                            var alertBuilder=AlertDialog.Builder(this@HomeActivity)
                            alertBuilder.setTitle("Update")
                            alertBuilder.setMessage("Do You Want Update Menu")
                            var itemView=LayoutInflater.from(this@HomeActivity).inflate(R.layout.update_layout,null)
                            var edtMenu=itemView.findViewById<EditText>(R.id.menu_Edt_dialog)
                            imgMenu=itemView.findViewById<ImageView>(R.id.menu_image_dialog)
                            Picasso.get().load(Comm.selectedMenu!!.image).into(imgMenu)

                            edtMenu.setText(Comm.selectedMenu!!.name)

                            imgMenu!!.setOnClickListener({
                                val cam_ImagesIntent = Intent(Intent.ACTION_GET_CONTENT)
                                cam_ImagesIntent.type = "image/*"
                                startActivityForResult(Intent.createChooser(cam_ImagesIntent,"Choose image"), 2)

                            })

                            alertBuilder.setNegativeButton("Cancel",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        p0!!.dismiss()
                                    }
                                })

                            alertBuilder.setPositiveButton("Update",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                                        if(edtMenu.text.toString().isEmpty()){
                                            edtMenu.setError("Please enter menu")
                                            edtMenu.requestFocus()
                                            return
                                        }
                                        var updateData=HashMap<String,Any>()
                                        updateData.put("name",edtMenu.text.toString())
                                        if (imgUri!=null){
                                            var progressDialog=ProgressDialog(this@HomeActivity)
                                            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                                            progressDialog.setTitle("Please Wait")
                                            progressDialog.setMessage("Loading...")
                                            progressDialog.max=100
                                            progressDialog.setCancelable(false)
                                            progressDialog.show()
                                            var storageRef=firebaseStorage.reference.child("images/"+UUID.randomUUID().toString())
                                            storageRef.putFile(imgUri!!).addOnFailureListener({

                                            }).addOnCompleteListener({
                                                storageRef.downloadUrl.addOnSuccessListener {uri->
                                                    updateData.put("image",uri.toString())
                                                    updateMenu(updateData,pos)
                                                    progressDialog.dismiss()
                                                }
                                            }).addOnProgressListener {
                                                var progress=(100.0)*it.bytesTransferred/it.totalByteCount
                                                progressDialog.incrementProgressBy(progress.toInt())

                                            }

                                        }else{

                                            updateMenu(updateData,pos)

                                        }
                                    }
                                })

                            alertBuilder.setView(itemView)
                            var alertDialog=alertBuilder.create()
                            alertDialog.show()

                        }
                    }))
            }
        })
//        itemTouchHelper!!.attachToRecyclerView(recyclerView)
    }

    private fun updateMenu(updateData: java.util.HashMap<String, Any> , pos:Int) {
//        Comm.selectedMenu!!.name=updateData.get("name") as String
//        if (updateData.get("image")!=null)
//            Comm.selectedMenu!!.image=updateData.get("image") as String
//
//        listOfMenus.set(pos, Comm.selectedMenu!!)
//        adapter.notifyItemChanged(pos)

        FirebaseDatabase.getInstance().reference
            .child("Category")
            .child(Comm.selectedMenu!!.key!!)
            .updateChildren(updateData).addOnFailureListener({
                Toast.makeText(this@HomeActivity,it.message,Toast.LENGTH_LONG).show()
            }).addOnCompleteListener({
//              homeViewModel!!.loadCategory()

                Comm.selectedMenu!!.name=updateData.get("name") as String
                if (updateData.get("image")!=null)
                    Comm.selectedMenu!!.image=updateData.get("image") as String
                listOfMenus.set(pos, Comm.selectedMenu!!)
                adapter.notifyItemChanged(pos)
                imgUri=null
            }).addOnFailureListener({
                Toast.makeText(this@HomeActivity,"${it.message}",Toast.LENGTH_LONG).show()
            })
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.order -> {
                startActivity(Intent(this,OrderActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 2 && resultCode== Activity.RESULT_OK) {
//            Toast.makeText(this,"Inside onActivityResult",Toast.LENGTH_LONG).show()
            if (data!=null){
                if (data.data != null){
                    imgUri=data.data
                    Picasso.get().load(imgUri).into(imgMenu)
                }
            }
        }
    }
}