package com.example.android.politicalpreparedness.navigator

import androidx.annotation.IdRes
import androidx.navigation.NavDirections

sealed class NavigationCommand {

    companion object {
        const val IS_REFRESH_HOME = "isRefreshHome"
    }

    data class To(val directions: NavDirections) : NavigationCommand()
    data class ParentTo(val directions: NavDirections) : NavigationCommand()
    data class BackTo(@IdRes val destinationId: Int, val inclusive: Boolean) : NavigationCommand()
    data class BackWithData(val key: String, val value: Any) : NavigationCommand()
    object Back : NavigationCommand()
    object ToRoot : NavigationCommand()
}

