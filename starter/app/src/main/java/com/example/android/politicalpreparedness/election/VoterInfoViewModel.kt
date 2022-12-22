package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.BR
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.base.Result
import com.example.android.politicalpreparedness.base.SingleLiveEvent
import com.example.android.politicalpreparedness.database.LocalRepository
import com.example.android.politicalpreparedness.base.Event
import com.example.android.politicalpreparedness.network.RemoteRepository
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import java.util.*
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    private val application: Application,
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : BaseViewModel() {

    //TODO: Add live data to hold voter info
    private val _voterInfo = MutableLiveData<Event<VoterInfoResponse>>()
    val voterInfo: LiveData<Event<VoterInfoResponse>>
        get() = _voterInfo

    val databaseFailureEvent = SingleLiveEvent<Boolean>()
    val databaseSuccessEvent = SingleLiveEvent<Boolean>()

    var election: Election? = null

    //TODO: Add var and methods to populate voter info
    @get:Bindable
    var electionName: String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.electionName)
            }
        }

    @get:Bindable
    var votingLocationFinderUrl: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.votingLocationFinderUrl)
            }
        }

    @get:Bindable
    var ballotInfoUrl: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.ballotInfoUrl)
            }
        }

    @get:Bindable
    var address: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.address)
            }
        }
    @get:Bindable
    var electionDay: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.electionDay)
            }
        }
    //TODO: Add var and methods to support loading URLs
    val urlClickEvent = SingleLiveEvent<String>()

    fun locationClick() {
        votingLocationFinderUrl?.let {
            urlClickEvent.value = it
        }
    }

    fun ballotClick() {
        ballotInfoUrl?.let {
            urlClickEvent.value = it
        }
    }

    //TODO: Add var and methods to save and remove elections to local database
    fun followButtonClick() {
        election?.let { election ->
            viewModelScope.launch {
                val isRemove = isSaved
                val isSuccess =
                    if (isRemove) localRepository.deleteElection(election) is Result.Success
                    else localRepository.saveElection(election) is Result.Success

                if (isSuccess) {
                    isSaved = !isSaved
                    databaseSuccessEvent.value = isRemove
                } else {
                    databaseFailureEvent.value = isRemove
                }
            }
        }

    }

    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
    @get:Bindable
    var isSaved: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.saved)
            }
        }

    private fun populateVoterInfo(election: Election, state: State? = null) {
        this.election = election
        electionName = election.name
        electionDay = election.electionDay
        votingLocationFinderUrl = state?.electionAdministrationBody?.votingLocationFinderUrl
            ?: application.getString(R.string.no_data_for_voting_url)
        ballotInfoUrl = state?.electionAdministrationBody?.ballotInfoUrl
            ?: application.getString(R.string.no_data_for_ballot_url)
        address = state?.electionAdministrationBody?.correspondenceAddress?.toFormattedString()
            ?: application.getString(R.string.no_data_for_address)
    }

    fun getVoterInfo(election: Election) {
        viewModelScope.launch {
            loadingEvent.postValue(true)

            val division = election.division
            val id = election.id
            val addressBuilder = StringBuilder("")
            if (division.isNotEmpty() && division.split("/").size >2) {
                addressBuilder.append(division.split("/")[2])
            }

            if (division.isNotEmpty() && division.split("/").size >1) {
                if (addressBuilder.isNotEmpty()) {
                    addressBuilder.append(", ")
                }
                addressBuilder.append(division.split("/")[1].split(":")[1])
            }

            val address = addressBuilder.toString()
            val result = remoteRepository.getVoterInfo(address, id)
            if (result is Result.Success) {
                _voterInfo.value = Event(result.data)
                populateVoterInfo(result.data.election, result.data.state?.get(0))
            } else {
                populateVoterInfo(election)
            }

            val getSavedElectionResult = localRepository.getElection(id)
            isSaved = getSavedElectionResult is Result.Success

            loadingEvent.postValue(false)
        }
    }
}