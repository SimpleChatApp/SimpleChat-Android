package com.kagan.chatapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kagan.chatapp.R
import com.kagan.chatapp.db.entities.UsersEntity
import com.kagan.chatapp.models.UserVM
import com.kagan.chatapp.utils.OnClickListener


class UserListAdapter
constructor(
    private val itemList: ArrayList<UsersEntity>,
    private val clickListener: OnClickListener<String>
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardViewLayout: CardView = view.findViewById<CardView>(R.id.cvLayout)
        val ivUserPics: ImageView = view.findViewById(R.id.ivUserPics)
        val tvUserOrTitle: TextView = view.findViewById(R.id.tvUsernameOrTitle)
        val tvDesc: TextView = view.findViewById(R.id.tvDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_view, parent, false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            cardViewLayout.setOnClickListener {
                clickListener.onclick(itemList[position].Id)
            }
            ivUserPics.setImageResource(R.drawable.ic_baseline_android_24)
            tvUserOrTitle.text = itemList[position].DisplayName
//            tvDesc.text = itemList[position]
        }
    }

    override fun getItemCount() = itemList.size
}