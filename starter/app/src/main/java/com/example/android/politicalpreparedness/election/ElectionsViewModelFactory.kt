package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.LocalRepository
import com.example.android.politicalpreparedness.network.RemoteRepository

//TODO: Create Factory to generate ElectionViewModel with provided election datasource
class ElectionsViewModelFactory(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
            ElectionsViewModel(remoteRepository, localRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}