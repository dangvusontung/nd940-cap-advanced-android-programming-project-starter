package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.network.RemoteRepository

class RepresentativeViewModelFactory(
    private val application: MyApplication,
    private val remoteRepository: RemoteRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            RepresentativeViewModel(application, remoteRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}