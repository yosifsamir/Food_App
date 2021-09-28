package com.example.myfoodapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfoodapp.R
import com.example.myfoodapp.adapter.CommentsAdapter
import com.example.myfoodapp.model.Comments
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentsBottomNavigation : BottomSheetDialogFragment() {
    private lateinit var listOfComments:List<Comments>
    private lateinit var layoutManager: LinearLayoutManager
    private var recyclerView: RecyclerView? =null
    private lateinit var commentsViewModel:CommentsViewModel
//    constructor(listOfComments:List<Comments>){
//        this.listOfComments=listOfComments
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        commentsViewModel=ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(CommentsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view= inflater.inflate(R.layout.comments_navigation_bottom,container,false)
        recyclerView=view.findViewById(R.id.commentsRecyclerView)
        layoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager
        commentsViewModel.foodCommentsLivData.observe(this, object : Observer<List<Comments>> {
            override fun onChanged(listOfComments: List<Comments>?) {
                if (listOfComments==null)
                    return
                var adapter=CommentsAdapter(listOfComments)
                recyclerView!!.adapter = adapter

            }
        })

        return view
    }
}