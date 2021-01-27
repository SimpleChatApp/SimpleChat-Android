package com.kagan.chatapp.utils

import androidx.navigation.NavController
import androidx.navigation.NavDirections

interface FragmentBasicFunction {

    fun navController(): NavController
    fun navigate(action: Int)
    fun navigate(action: NavDirections)
    fun displayProgressBar(b: Boolean)
    fun navigateUp()
}