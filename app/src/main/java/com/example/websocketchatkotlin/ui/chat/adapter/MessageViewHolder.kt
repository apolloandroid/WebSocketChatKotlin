package com.example.websocketchatkotlin.ui.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.websocketchatkotlin.R
import kotlinx.android.synthetic.main.message_list_item_sent.view.*

private const val RESOURCE_ID_SENT = R.layout.message_list_item_sent

abstract class MessageViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    open val messageSender: TextView = itemView.text_message_sender
    open val messageTime: TextView = itemView.text_message_time
    open val messageText: TextView = itemView.text_message_text

    companion object {
        fun getInstance(parent: ViewGroup, resourceId: Int): MessageViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return when (resourceId) {
                RESOURCE_ID_SENT -> MessageSentViewHolder(
                    inflater.inflate(resourceId, parent, false)
                )
                else -> MessageReceivedViewHolder(
                    inflater.inflate(resourceId, parent, false)
                )
            }
        }
    }
}

class MessageSentViewHolder(itemView: View) : MessageViewHolder(itemView)

class MessageReceivedViewHolder(itemView: View) : MessageViewHolder(itemView)