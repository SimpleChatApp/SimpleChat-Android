package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.kagan.chatapp.R
import com.kagan.chatapp.databinding.FragmentEditRoomBinding
import com.kagan.chatapp.models.APIResultErrorCodeVM
import com.kagan.chatapp.models.chatrooms.ChatRoomUpdateVM
import com.kagan.chatapp.models.chatrooms.ChatRoomVM
import com.kagan.chatapp.utils.States
import com.kagan.chatapp.utils.ErrorCodes
import com.kagan.chatapp.utils.Utils.hideKeyboard
import com.kagan.chatapp.viewmodels.ChatRoomsViewModel
import com.kagan.chatapp.viewmodels.TokenPreferenceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditRoomFragment : Fragment(R.layout.fragment_edit_room) {

    private lateinit var binding: FragmentEditRoomBinding
    private val chatRoomsViewModel: ChatRoomsViewModel by viewModels()
    private val tokenPreferenceViewModel: TokenPreferenceViewModel by viewModels()
    private val safeArgs: ChatRoomFragmentArgs by navArgs()
    private lateinit var auth: String
    private lateinit var users: List<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditRoomBinding.bind(view)

        init()
        subscribe()
    }

    private fun init() {

        binding.editRoom.btnCreateEdit.text = getString(R.string.edit)
        binding.editRoom.topAppBar.title = getString(R.string.edit)

        binding.editRoom.btnCreateEdit.setOnClickListener {
            editRoom()
            hideKeyboard(requireContext(), it)
        }

        binding.editRoom.topAppBar.setNavigationOnClickListener {
            navigateUp()
        }

        binding.editRoom.topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> {
                    chatRoomsViewModel.deleteChatRoom(auth, safeArgs.chatRoomId.id)
                    true
                }
                else -> false
            }
        }
    }

    private fun subscribe() {
        chatRoomsViewModel.chatRoom.observe(viewLifecycleOwner, { state ->
            when (state) {
                is States.Loading -> {
                    displayProgressBar(true)
                }
                is States.Success -> {
                    setEditField(state.data)
                    displayProgressBar(false)
                }
                else -> displayProgressBar(false)
            }
        })

        chatRoomsViewModel.chatRoomFailed.observe(viewLifecycleOwner, { state ->
            when (state) {
                is States.Error -> {
                    displayProgressBar(false)
                    Log.d(TAG, "subscribe: error ${state.error}")
                }
                else -> displayProgressBar(false)
            }
        })

        chatRoomsViewModel.putState.observe(viewLifecycleOwner, { state ->
            when (state) {
                is States.Loading -> {
                    displayProgressBar(true)
                }
                is States.Success -> {
                    displayProgressBar(false)
                    displaySnackBar(R.string.chat_room_updated_successfully)
                }
                is States.Error -> {
                    displayProgressBar(false)
                    displayErrors(state.error.Errors!!)
                    displaySnackBar(R.string.chat_room_updated_failed)
                }
                else -> displayProgressBar(false)
            }
        })

        chatRoomsViewModel.deleteState.observe(viewLifecycleOwner, { state ->
            when (state) {
                is States.Loading -> {
                    displayProgressBar(true)
                }
                is States.Success -> {
                    displaySnackBar(R.string.chat_room_deleted_successfully)
                    navigate()
                }
                is States.Error -> {
                    displayProgressBar(false)
                    displayErrors(state.error.Errors!!)
                    displaySnackBar(R.string.chat_room_deleted_successfully)
                }
                else -> displayProgressBar(false)
            }
        })

        tokenPreferenceViewModel.accessToken.observe(viewLifecycleOwner, { accessToken ->
            accessToken?.let {
                auth = it
                chatRoomsViewModel.getChatRoom(it, safeArgs.chatRoomId.id)
            }
        })
    }

    private fun setEditField(data: ChatRoomVM) {
        binding.editRoom.evName.hint = data.Name
        binding.editRoom.evDescription.hint = data.Description
        binding.editRoom.switchPrivate.isChecked = data.IsPrivate

        users = data.Users
    }

    private fun getEditField(): ChatRoomUpdateVM {
        val name = binding.editRoom.evName.editText?.text.toString()
        val description = binding.editRoom.evDescription.editText?.text.toString()
        val isPrivate = binding.editRoom.switchPrivate.isChecked

        // todo DI
        return ChatRoomUpdateVM(
            Name = name,
            IsMain = false,
            IsPrivate = isPrivate,
            Description = description,
            IsOneToOneChat = false,
            Users = users
        )
    }

    private fun displayProgressBar(b: Boolean) {
        if (b) {
            binding.progressEditRoom.visibility = View.VISIBLE
            binding.editRoom.root.visibility = View.GONE
        } else {
            binding.progressEditRoom.visibility = View.GONE
            binding.editRoom.root.visibility = View.VISIBLE
        }
    }

    private fun displayErrors(errors: List<APIResultErrorCodeVM>) {
        errors.forEach {
            when (it.Field) {
                "General" -> {
                    displaySnackBar(R.string.chat_room_cannot_deleted)
                }
                "Name" -> {
                    binding.editRoom.evName.error =
                        ErrorCodes.getDescription(it.ErrorCode, requireContext())
                }
            }
        }
    }

    private fun displaySnackBar(text: Int) {
        Snackbar.make(
            requireView(),
            getString(text),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun editRoom() {
        chatRoomsViewModel.putChatRoom(auth, safeArgs.chatRoomId.id, getEditField())
    }

    private fun navController() = findNavController()

    private fun navigateUp() {
        navController().navigateUp()
    }

    private fun navigate() {
        navController().navigate(R.id.action_editRoomFragment_to_chatRoomsFragment)
    }
}