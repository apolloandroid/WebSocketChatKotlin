package com.example.websocketchatkotlin.ui.chat

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.websocketchatkotlin.R
import com.example.websocketchatkotlin.databinding.ChatFragmentBinding
import com.example.websocketchatkotlin.repository.Repository
import com.example.websocketchatkotlin.repository.network.ConnectionStatus
import com.example.websocketchatkotlin.ui.chat.adapter.MessageListAdapter
import com.google.android.material.snackbar.Snackbar


class ChatFragment : androidx.fragment.app.Fragment() {
    private lateinit var userName: String
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var binding: ChatFragmentBinding
    private val DEFAULT_USERNAME = "Guest"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_fragment, container, false)
        setChatViewModel()
        setMessagesList(binding.messagesList)
        userName = setUserName()
        initObservers()
        binding.buttonSendMessage.setOnClickListener { onSendMessageListener() }
        return binding.root
    }

    private fun setChatViewModel() {
        val application: Application = requireNotNull(activity).application
        val repository = Repository.getInstance(application)
        chatViewModel = ChatViewModel.getInstance(application, repository)
    }

    private fun setMessagesList(recyclerView: RecyclerView) {
        recyclerView.adapter = MessageListAdapter
    }

    private fun setUserName(): String {
        userName = ChatFragmentArgs.fromBundle(arguments!!).userName
        if (userName.isEmpty()) userName = DEFAULT_USERNAME
        return userName
    }

    private fun initObservers() {

        // !!! Extract StringResources !!!
        chatViewModel.connectionStatus?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                ConnectionStatus.OK -> Snackbar.make(
                    binding.root, "Connection established",
                    Snackbar.LENGTH_SHORT
                ).show()
                ConnectionStatus.FAILED -> Snackbar.make(
                    binding.root,
                    "Connection failed. Check Internet access",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })

        chatViewModel.messageList?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            MessageListAdapter.submitList(it)
        })

        chatViewModel.newMessageReceived?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            chatViewModel.receiveNewMessage()
        })
    }

    private fun onSendMessageListener() {
        if (binding.editMessageInput.text.isNotEmpty()) {
            val messageSender = userName
            val messageText = binding.editMessageInput.text.toString()
            chatViewModel.sendNewMessage(messageText, messageSender)
            binding.editMessageInput.text.clear()
        }
    }
}



