package com.example.android.politicalpreparedness.navigator

import android.content.Context
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.base.BaseActivity

class NavigatorImpl : Navigator {

    private var activityContext: Context? = null
    private val activity get() = (activityContext as? BaseActivity)
    private val fragment get() = activity?.supportFragmentManager?.fragments?.firstOrNull()
    override val navController get() = fragment?.findNavController()

    override fun setContext(context: Context?) {
        this.activityContext = context
    }
}
