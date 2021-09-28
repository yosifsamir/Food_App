package com.example.myfoodapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfoodapp.R
import com.example.myfoodapp.model.Comments

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    var listOfComments:List<Comments> ? =null

    constructor(listOfComments: List<Comments>?) : super() {
        this.listOfComments = listOfComments
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.comments_layout,parent,false)
        return CommentsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfComments!!.size
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.userNameCommentTxt.text= this!!.listOfComments!![position].userName
        holder.commentTxt.text= this!!.listOfComments!![position].comment
//        holder.timeStampTxt.text= this!!.listOfComments!![position].timestamp
    }


    inner class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userNameCommentTxt=itemView.findViewById<TextView>(R.id.userNameCommentTxt)
        var commentTxt=itemView.findViewById<TextView>(R.id.commentTxt)
        var timeStampTxt=itemView.findViewById<TextView>(R.id.timeStampTxt)
    }

}