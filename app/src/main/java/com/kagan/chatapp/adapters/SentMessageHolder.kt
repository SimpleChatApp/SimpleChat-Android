package com.kagan.chatapp.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kagan.chatapp.R
import com.kagan.chatapp.models.chatrooms.MessageVM
import com.kagan.chatapp.utils.Utils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SentMessageHolder
constructor(
    view: View
) : RecyclerView.ViewHolder(view) {
    private var textChatDateMe: TextView? = null
    private var textChatMessageMe: TextView? = null
    private var textChatTimestampMe: TextView? = null

    init {
        textChatDateMe = itemView.findViewById(R.id.text_gchat_date_me)
        textChatMessageMe = itemView.findViewById(R.id.text_gchat_message_me)
        textChatTimestampMe = itemView.findViewById(R.id.text_gchat_timestamp_me)
    }

    private fun formatDate(date: String) = Utils.formatDate(date)

    private fun formatTime(time: String) = Utils.formatTime(time, itemView.context)

    fun bind(sent: MessageVM) {
        sent.apply {
            textChatDateMe?.text = formatDate(CreateDT)
            textChatMessageMe?.text = Text
            textChatTimestampMe?.text = formatTime(CreateDT)
        }
    }
}

