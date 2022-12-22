package com.example.android.politicalpreparedness.launch

import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.navigator.NavigationCommand

class LaunchViewModel : BaseViewModel() {

    fun navToElections() {
        navigate(
            NavigationCommand.To(
                LaunchFragmentDirections.actionLaunchFragmentToElectionsFragment()
            )
        )
    }

    fun navToRepresentatives() {
        navigate(
            NavigationCommand.To(
                LaunchFragmentDirections.actionLaunchFragmentToRepresentativeFragment()
            )
        )
    }
}