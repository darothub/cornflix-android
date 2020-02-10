package com.darotapp.cornflix.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.darotapp.cornflix.R

class SwipeAdapter():RecyclerView.Adapter<SwipeAdapter.SwipeHolder>() {
    private var texts = arrayListOf<String>("hello")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.swipe_recycler_item, parent, false)
        return SwipeHolder(itemView)
    }

    override fun getItemCount(): Int {
       return 1
    }



    override fun onBindViewHolder(holder: SwipeHolder, position: Int) {
//        var currentEl = texts[position]
        holder.bind()
    }


    class SwipeHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val btn = itemView.findViewById<Button>(R.id.btn)

        fun bind(){

        }

    }

}