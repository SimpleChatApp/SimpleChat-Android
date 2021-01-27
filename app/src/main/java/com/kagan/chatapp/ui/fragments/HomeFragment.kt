package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kagan.chatapp.R
import com.kagan.chatapp.databinding.FragmentHomeBinding
import com.kagan.chatapp.utils.UserEvent
import com.kagan.chatapp.viewmodels.LoginViewModel
import com.kagan.chatapp.viewmodels.SharedViewModel
import com.kagan.chatapp.viewmodels.TokenPreferenceViewModel
import com.kagan.chatapp.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var sharedViewModel: SharedViewModel
    private val tokenPreferenceViewModel: TokenPreferenceViewModel by viewModels()
    private lateinit var accessToken: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        init()
        subscribe()
        getUser()
    }

    private fun getUser() {
        sharedViewModel.currentUser.observe(viewLifecycleOwner, Observer {
            val currentUser = it ?: return@Observer
            userViewModel.getUser(currentUser)
        })
    }

    private fun subscribe() {
        userViewModel.isLoading.observe(viewLifecycleOwner, {
            when (it) {
                is UserEvent.Loading -> {
                    showProgressBar()
                }
                is UserEvent.Valid -> {
                    hideProgressBar()
                }
            }
        })

        userViewModel.user.observe(viewLifecycleOwner, {
            binding.tvUserName.text = it.Rec?.DisplayName
        })

        tokenPreferenceViewModel.accessToken.observe(viewLifecycleOwner, Observer {
            val token = it ?: return@Observer
            accessToken = token
        })


    }

    private fun hideProgressBar() {
        binding.progressBarTest.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBarTest.visibility = View.VISIBLE
    }

    private fun init() {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        binding.btnChatRooms.setOnClickListener {
            navigate(R.id.action_homeFragment_to_chatRoomsFragment)
        }
        binding.btnUsers.setOnClickListener {
            navigate(R.id.action_homeFragment_to_usersListFragment)
        }
        binding.btnSettings.setOnClickListener { }

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun navigate(action: Int) {
        findNavController().navigate(action)
    }

    private fun logout() {
        loginViewModel.logout(accessToken)
        tokenPreferenceViewModel.storeAccessToken("")
        tokenPreferenceViewModel.storeRefreshToken("")

        navigate(R.id.action_homeFragment_to_loginFragment)
    }
}