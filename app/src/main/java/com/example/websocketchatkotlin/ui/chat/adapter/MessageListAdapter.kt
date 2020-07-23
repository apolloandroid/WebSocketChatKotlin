package com.example.websocketchatkotlin.ui.chat.adapter


import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.websocketchatkotlin.R
import com.example.websocketchatkotlin.repository.database.Message
import com.example.websocketchatkotlin.repository.database.MessageType


object MessageListAdapter :
    ListAdapter<Message, MessageViewHolder>(MessageDiffCallBack()) {
    private const val MESSAGE_TYPE_SENT = 0
    private const val MESSAGE_TYPE_RECEIVED = 1
    private const val MESSAGE_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return when (viewType) {
            MESSAGE_TYPE_SENT -> MessageViewHolder.getInstance(
                parent,
                R.layout.message_list_item_sent
            )
            else -> MessageViewHolder.getInstance(parent, R.layout.message_list_item_received)
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)
        holder.messageText.text = message.messageText
        holder.messageTime.text = android.text.format.DateFormat.format(
            MESSAGE_DATE_FORMAT,
            message.messageTime
        )
        holder.messageSender.text = message.messageSender
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        when (message.messageType) {
            MessageType.SENT.toString() -> return MESSAGE_TYPE_SENT
            MessageType.RECEIVED.toString() -> return MESSAGE_TYPE_RECEIVED
        }
        return super.getItemViewType(position)
    }

      class MessageDiffCallBack : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.messageId == newItem.messageId
        }

        override fun areContentsTheSame(
            oldItem: Message,
            newItem: Message
        ): Boolean {
            return oldItem.messageText == newItem.messageText
        }
    }
}