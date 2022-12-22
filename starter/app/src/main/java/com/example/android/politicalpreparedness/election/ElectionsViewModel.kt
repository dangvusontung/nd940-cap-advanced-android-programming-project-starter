package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.base.Event
import com.example.android.politicalpreparedness.base.Result
import com.example.android.politicalpreparedness.base.data
import com.example.android.politicalpreparedness.database.LocalRepository
import com.example.android.politicalpreparedness.navigator.NavigationCommand
import com.example.android.politicalpreparedness.network.RemoteRepository
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : BaseViewModel() {

    //TODO: Create live data val for upcoming elections
    private val _upcomingElections =
        MutableLiveData<Event<List<Election>>>()
    val upcomingElections: LiveData<Event<List<Election>>>
        get() = _upcomingElections

    //TODO: Create live data val for saved elections
    private val _savedElections =
        MutableLiveData<Event<List<Election>>>()
    val savedElections: LiveData<Event<List<Election>>>
        get() = _savedElections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    fun getElectionData() {
        viewModelScope.launch {
            loadingEvent.postValue(true)
            val response = remoteRepository.getElections()
            if (response is Result.Success) {
                _upcomingElections.value = Event(response.data)
            }

            val localData = localRepository.getElections().data ?: listOf()
            _savedElections.value = Event(localData)
            loadingEvent.postValue(false)
        }
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info
    fun navToVoteInfo(election: Election) {
        navigate(
            NavigationCommand.To(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                    election
                )
            )
        )
    }
}