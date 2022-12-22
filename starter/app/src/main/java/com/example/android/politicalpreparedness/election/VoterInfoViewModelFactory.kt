package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.LocalRepository
import com.example.android.politicalpreparedness.network.RemoteRepository

//TODO: Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(private val application: Application,
                                private val remoteRepository: RemoteRepository,
                                private val localRepository: LocalRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            VoterInfoViewModel(application, remoteRepository, localRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}