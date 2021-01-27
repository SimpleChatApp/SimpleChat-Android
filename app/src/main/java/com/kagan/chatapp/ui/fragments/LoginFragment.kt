package com.kagan.chatapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kagan.chatapp.R
import com.kagan.chatapp.databinding.FragmentLoginBinding
import com.kagan.chatapp.models.LoginRequestVM
import com.kagan.chatapp.models.LoginUserRequestVM
import com.kagan.chatapp.utils.ErrorCodes
import com.kagan.chatapp.utils.UserEvent
import com.kagan.chatapp.utils.Utils.hideKeyboard
import com.kagan.chatapp.utils.Utils.showApiFailure
import com.kagan.chatapp.viewmodels.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

const val TAG = "LoginFragment"

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val tokenPreferenceViewModel: TokenPreferenceViewModel by viewModels()
    private val userPreferenceViewModel: CurrentUserViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var sharedViewModel: SharedViewModel
    private val userViewModel: UserViewModel by viewModels()
    private var auth = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        init()
    }

    private fun init() {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        setFocusChangeListener()

        binding.layoutId.btnLogin.setOnClickListener {
            login()
            hideKeyboard(requireContext(), requireView())
        }

        binding.layoutId.btnRegister.setOnClickListener {
            register()
        }

        binding.layoutId.btnForgot.setOnClickListener {
            forgotPassword()
        }

        subscribe()
    }

    private fun subscribe() {

        tokenPreferenceViewModel.accessToken.observe(viewLifecycleOwner, { accessToken ->
            if (accessToken != null) {
                loginViewModel.checkTokenIsValid(accessToken)
                auth = accessToken
            } else {
                loginViewModel.setState(UserEvent.NotValid)
            }
        })

        loginViewModel.isValid.observe(viewLifecycleOwner, {
            if (it) {
                navigate(R.id.action_loginFragment_to_homeFragment)
            }
        })

        loginViewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is UserEvent.Loading -> {
                    displayProgressBar(true)
                }
                is UserEvent.Valid -> {
                    displayProgressBar(false)
                    userViewModel.getUsers(auth)
                }
                is UserEvent.NotValid -> {
                    displayLayout()
                    displayProgressBar(false)
                }
            }
        })

        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer
            setVisibilityProgress(false)

            tokenPreferenceViewModel.storeAccessToken(loginResult.TokenData.AccessToken)
            tokenPreferenceViewModel.storeRefreshToken(loginResult.TokenData.RefreshToken)
            var about = ""
            if (loginResult.About != null) {
                about = loginResult.About
            }
            userPreferenceViewModel.storeUser(
                username = loginResult.UserName,
                displayName = loginResult.DisplayName,
                about = about,
                accessToken = loginResult.TokenData.AccessToken,
                refreshToken = loginResult.TokenData.RefreshToken,
                id = loginResult.Id.toString()
            )

            sharedViewModel.postValue(loginResult)
            loginViewModel.clearResult()
            navigate(R.id.action_loginFragment_to_homeFragment)
        })

        loginViewModel.loginFailure.observe(viewLifecycleOwner, Observer {
            val loginFailure = it ?: return@Observer
            if (!loginFailure) {
                setVisibilityProgress(loginFailure)
                showApiFailure(requireContext(), requireView())
            }

            loginViewModel.clearFailure()
        })

        loginViewModel.loginErrors.observe(viewLifecycleOwner, Observer { result ->
            val loginError = result ?: return@Observer
            Log.d(TAG, "loginErrors: $loginError")
            setVisibilityProgress(false)
            loginError.Errors?.forEach {
                when (it.Field) {
                    "UserName" -> {
                        binding.layoutId.evUserName.error =
                            ErrorCodes.getDescription(it.ErrorCode, requireContext())
                    }

                    "Password" -> {
                        binding.layoutId.evPassword.error =
                            ErrorCodes.getDescription(it.ErrorCode, requireContext())
                    }
                    "General" -> {
                        binding.layoutId.evUserName.error =
                            ErrorCodes.getDescription(it.ErrorCode, requireContext())
                    }
                }
            }
            loginViewModel.clearError()
        })
    }

    private fun displayProgressBar(b: Boolean) {
        binding.layoutProgress.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun displayLayout() {
        binding.layoutId.root.visibility = View.VISIBLE
    }

    private fun forgotPassword() {
        navigate(R.id.action_loginFragment_to_passwordRequestFragment)
    }

    private fun register() {
        navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun navigate(action: Int) {
        findNavController().navigate(action)
    }

//    private fun navigate() {
//        parentFragmentManager.commit {
//            replace<HomeFragment>(R.id.fragment)
//            setReorderingAllowed(true)
//        }
//    }

    private fun login() {
        if (usernameIsNotEmpty() && passwordIsNotEmpty()) {
            clearErrorMessage()
            setVisibilityProgress(true)
            val username = binding.layoutId.evUserName.editText?.text!!.toString()
            val password = binding.layoutId.evPassword.editText?.text!!.toString()

            val loginRequestVM = LoginRequestVM(username, password)
            // todo DI
            val l = LoginUserRequestVM()
            l.requestBody = loginRequestVM

            loginViewModel.login(l)
        } else {
            setErrorMessage()
        }
    }

    private fun setVisibilityProgress(value: Boolean) {
        if (value) {
            binding.layoutId.progress.visibility = View.VISIBLE
        } else {
            binding.layoutId.progress.visibility = View.INVISIBLE
        }
    }

    private fun setErrorMessage() {
        if (!usernameIsNotEmpty()) {
            binding.layoutId.evUserName.error =
                getString(R.string.evErrorMessage, getString(R.string.user_name))
        } else {
            binding.layoutId.evUserName.error = null
        }

        if (!passwordIsNotEmpty()) {
            binding.layoutId.evPassword.error =
                getString(R.string.evErrorMessage, getString(R.string.password))
        } else {
            binding.layoutId.evPassword.error = null
        }
    }

    private fun clearErrorMessage() {
        binding.layoutId.evUserName.error = null
        binding.layoutId.evPassword.error = null
    }

    private fun usernameIsNotEmpty(): Boolean {
        return binding.layoutId.evUserName.editText?.text?.isNotEmpty()!!
    }

    private fun passwordIsNotEmpty(): Boolean {
        return binding.layoutId.evPassword.editText?.text?.isNotEmpty()!!
    }

    private fun setFocusChangeListener() {
        val evUsername = binding.layoutId.evUserName
        val evPassword = binding.layoutId.evPassword

        evUsername.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                evUsername.error = null
            }
        }


        evPassword.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                evPassword.error = null
            }
        }
    }
}