package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kagan.chatapp.R
import com.kagan.chatapp.databinding.FragmentCreateRoomBinding
import com.kagan.chatapp.models.chatrooms.AddVM
import com.kagan.chatapp.utils.States
import com.kagan.chatapp.utils.ErrorCodes.getDescription
import com.kagan.chatapp.utils.Utils.hideKeyboard
import com.kagan.chatapp.viewmodels.ChatRoomsViewModel
import com.kagan.chatapp.viewmodels.TokenPreferenceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateRoomFragment : Fragment(R.layout.fragment_create_room) {

    private lateinit var binding: FragmentCreateRoomBinding
    private val chatRoomsViewModel: ChatRoomsViewModel by viewModels()
    private val tokenPreferenceViewModel: TokenPreferenceViewModel by viewModels()

    private var accessToken = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateRoomBinding.bind(view)

        init()
        subscribe()
    }

    private fun init() {
        binding.createRoom.btnCreateEdit.text = getString(R.string.create)
        binding.createRoom.topAppBar.title = getString(R.string.create)

        binding.createRoom.btnCreateEdit.setOnClickListener {
            hideKeyboard(requireContext(), it)
            createRoom()
        }

        binding.createRoom.topAppBar.setNavigationOnClickListener {
            navigateUp()
        }
    }

    private fun navController() = findNavController()

    private fun navigateUp() {
        navController().navigateUp()
    }

    private fun subscribe() {
        tokenPreferenceViewModel.accessToken.observe(viewLifecycleOwner, {
            it?.let {
                accessToken = it
            }
        })

        chatRoomsViewModel.postState.observe(viewLifecycleOwner, { postState ->
            when (postState) {
                is States.Loading -> {
                    displayProgressBar(true)
                }
                is States.Success -> {
                    navigateUp()
                }
                is States.Error -> {
                    displayProgressBar(false)
                    postState.error.Errors?.forEach {
                        when (it.Field) {
                            "Name" -> {
                                binding.createRoom.evName.error =
                                    getDescription(it.ErrorCode, requireContext())
                            }
                        }
                    }
                }
            }
        })
    }


    private fun getRoom(): AddVM {
        val name = binding.createRoom.evName.editText?.text.toString()
        val description = binding.createRoom.evDescription.editText?.text.toString()
        val isPrivate = binding.createRoom.switchPrivate.isChecked
        val users = listOf<String>(
            // todo will choose from list
            "4e189867-cf61-45a0-8ca3-8510499ccfcb",
            "810da6fd-fd25-4042-bd56-89913581af47"
        )

        // DI and IsMain, IsOneToOne
        return AddVM(
            Description = description,
            IsMain = false,
            IsPrivate = isPrivate,
            Users = users,
            IsOneToOneChat = false,
            Name = name
        )

    }

    private fun createRoom() {
        chatRoomsViewModel.postChatRooms(accessToken, getRoom())
    }

    private fun displayProgressBar(b: Boolean) {
        if (b) {
            binding.progressCreateRoom.visibility = View.VISIBLE
            binding.createRoom.root.visibility = View.GONE
        } else {
            binding.progressCreateRoom.visibility = View.GONE
            binding.createRoom.root.visibility = View.VISIBLE
        }
    }
}