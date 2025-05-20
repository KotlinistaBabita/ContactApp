package com.example.contactapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.EditActivity
import com.example.contactapp.R
import com.example.contactapp.data.model.UsersItem

class NewContactAdapter ( private val contactList: List<UsersItem>,
                          private val context: Context

) : RecyclerView.Adapter<NewContactAdapter.NewViewHolder>() {
    private val colorList = listOf(
        R.color.cardColor1,
        R.color.cardColor2,
        R.color.cardColor3,
        R.color.cardColor4,
        R.color.cardColor5
    )

    class NewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvProfession: TextView = itemView.findViewById(R.id.tvProfession)
        val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val imgEdit: ImageView = itemView.findViewById(R.id.imgEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_new, parent, false)
        return NewViewHolder(view)
    }

    override fun getItemCount(): Int = contactList.size

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        val contact = contactList[position]
        holder.tvName.text = contact.fullName
        holder.tvProfession.text = contact.course
        holder.tvPhone.text = contact.phone
        holder.tvEmail.text = contact.email
        val colorResId = colorList[position % colorList.size]
        val cardView = holder.itemView.findViewById<androidx.cardview.widget.CardView>(R.id.cardContact)
        cardView.setCardBackgroundColor(holder.itemView.context.getColor(colorResId))


        holder.imgEdit.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java).apply {
                putExtra("fullName", contact.fullName)
                putExtra("course", contact.course)
                putExtra("phone", contact.phone)
                putExtra("email", contact.email)
                putExtra("userId", contact.id)
            }
            context.startActivity(intent)
        }
    }


}