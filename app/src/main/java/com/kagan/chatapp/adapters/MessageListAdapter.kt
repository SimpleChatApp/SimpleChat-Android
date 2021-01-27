package com.kagan.chatapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kagan.chatapp.R
import com.kagan.chatapp.db.entities.UsersEntity
import com.kagan.chatapp.models.chatrooms.MessageVM
import java.util.*

class MessageListAdapter
constructor(
    private val context: Context,
    private val messageList: List<MessageVM>,
    private val currentId: String,
    private val userList: List<UsersEntity>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_MESSAGE_SENT = 1
    private val VIEW_TYPE_MESSAGE_RECEIVED = 2

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view: View
        return if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_me, parent, false)
            SentMessageHolder(view)
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_other, parent, false)
            ReceivedMessageHolder(view, userList)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]

        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> {
                SentMessageHolder(holder.itemView).bind(message)
            }
            VIEW_TYPE_MESSAGE_RECEIVED -> {
                ReceivedMessageHolder(holder.itemView, userList).bind(message)
            }
        }
    }

    override fun getItemCount(): Int = messageList.size

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]

        return if (message.CreateBy.toString() == currentId) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }
}