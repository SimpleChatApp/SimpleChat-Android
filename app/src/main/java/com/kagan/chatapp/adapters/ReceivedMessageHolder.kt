package com.kagan.chatapp.adapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kagan.chatapp.R
import com.kagan.chatapp.db.entities.UsersEntity
import com.kagan.chatapp.models.chatrooms.MessageVM
import com.kagan.chatapp.utils.Utils
import io.sentry.Sentry
import io.sentry.SentryLevel
import java.lang.NullPointerException
import java.util.*

class ReceivedMessageHolder
constructor(view: View, private val userList: List<UsersEntity>) : RecyclerView.ViewHolder(view) {
    private var textChatDateOther: TextView? = null
    private var imageChatProfileOther: ImageView? = null
    private var textChatUserOther: TextView? = null
    private var textChatMessageOther: TextView? = null
    private var textChatTimestampOther: TextView? = null

    init {
        textChatDateOther = itemView.findViewById(R.id.text_gchat_date_other)
        imageChatProfileOther = itemView.findViewById(R.id.image_gchat_profile_other)
        textChatUserOther = itemView.findViewById(R.id.text_gchat_user_other)
        textChatMessageOther = itemView.findViewById(R.id.text_gchat_message_other)
        textChatTimestampOther = itemView.findViewById(R.id.text_gchat_timestamp_other)
    }

    private fun formatDate(date: String) = Utils.formatDate(date)

    private fun formatTime(time: String) = Utils.formatTime(time, itemView.context)

    fun bind(received: MessageVM) {
        received.apply {
            textChatDateOther?.text = formatDate(CreateDT)
            textChatUserOther?.text = findUserName(CreateBy)
            textChatMessageOther?.text = Text
            textChatTimestampOther?.text = formatTime(CreateDT)
        }
    }

    private fun findUserName(id: UUID): String {
        val idStr = id.toString()
        try {
            val user = userList.find {
                it.Id == idStr
            }
            return user?.DisplayName!!
        } catch (e: NullPointerException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        }
        return ""
    }
}