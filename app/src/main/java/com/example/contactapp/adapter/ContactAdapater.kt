package com.example.contactapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.R
import com.example.contactapp.data.model.Contact

class ContactAdapter(private var users: List<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val tvCircle: TextView = itemView.findViewById(R.id.tvCircle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val user = users[position]
        holder.name.text = user.name
        holder.tvCircle.text= user.name.firstOrNull()?.toString()?.uppercase() ?: ""
    }

    override fun getItemCount(): Int = users.size

    fun updateData(newList: List<Contact>) {
        this.users = newList
        notifyDataSetChanged()
    }
}