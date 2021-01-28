package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.kagan.chatapp.R
import com.kagan.chatapp.adapters.UserListAdapter
import com.kagan.chatapp.databinding.FragmentUsersListBinding
import com.kagan.chatapp.db.entities.UsersEntity
import com.kagan.chatapp.models.UserVM
import com.kagan.chatapp.utils.FragmentBasicFunction
import com.kagan.chatapp.utils.OnClickListener
import com.kagan.chatapp.utils.States
import com.kagan.chatapp.viewmodels.DatabaseViewModel
import com.kagan.chatapp.viewmodels.TokenPreferenceViewModel
import com.kagan.chatapp.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UsersListFragment : Fragment(R.layout.fragment_users_list), OnClickListener<String>,
    FragmentBasicFunction {

    private lateinit var usersAdapter: UserListAdapter
    private val tokenPreferenceViewModel: TokenPreferenceViewModel by viewModels()
    private lateinit var binding: FragmentUsersListBinding
    private val usersViewModel: UserViewModel by viewModels()
    private val databaseViewModel: DatabaseViewModel by viewModels()
    private var usersList = arrayListOf<UsersEntity>()
    private var usersListDao = arrayListOf<UserVM>()
    private var auth = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUsersListBinding.bind(view)

        init()
        subscribe()
    }

    private fun init() {
        usersAdapter = UserListAdapter(usersList, this)
        binding.userList.topAppBar.title = getString(R.string.users)
        binding.userList.chatRoomRecyclerView.adapter = usersAdapter

        binding.userList.topAppBar.setNavigationOnClickListener {
            navigateUp()
        }
    }

    private fun subscribe() {
        databaseViewModel.usersStates.observe(viewLifecycleOwner, { state ->
            when (state) {
                is States.Loading -> {
                    displayProgressBar(true)
                }
                is States.Success -> {
                    displayProgressBar(false)
                    usersList.addAll(state.data)
                    usersAdapter.notifyDataSetChanged()
                }
                else -> displayProgressBar(false)
            }
        })

        tokenPreferenceViewModel.accessToken.observe(viewLifecycleOwner, { accessToken ->
            accessToken?.let {
                auth = it
                databaseViewModel.getUsers()
                usersViewModel.getUsers(auth)
            }
        })

        usersViewModel.usersStates.observe(viewLifecycleOwner, { state ->
            when (state) {
                is States.Success -> {
                    usersListDao.addAll(state.data)

                }
                else -> displayProgressBar(false)
            }
        })
    }

    override fun onclick(id: String) {
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show()
    }

    override fun navController(): NavController {
        return findNavController()
    }

    override fun navigate(action: Int) {
        navController().navigate(action)
    }

    override fun navigate(action: NavDirections) {
        navController().navigate(action)
    }

    override fun displayProgressBar(b: Boolean) {
        if (b) {
            binding.userListProgressBar.visibility = View.VISIBLE
            binding.userList.root.visibility = View.GONE
        } else {
            binding.userListProgressBar.visibility = View.GONE
            binding.userList.root.visibility = View.VISIBLE
        }
    }

    override fun navigateUp() {
        navController().navigateUp()
    }
}