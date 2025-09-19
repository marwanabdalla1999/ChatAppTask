package com.palm.chatapp.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.palm.chatapp.R

class MessagesAdapter(private val items: List<String>) :
    RecyclerView.Adapter<MessagesAdapter.MessageVH>() {

    class MessageVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt: TextView = itemView.findViewById(R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageVH(v)
    }

    override fun onBindViewHolder(holder: MessageVH, position: Int) {
        holder.txt.text = items[position]
    }

    override fun getItemCount(): Int = items.size
}
