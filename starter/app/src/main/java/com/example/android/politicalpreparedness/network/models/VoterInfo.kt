package com.example.android.politicalpreparedness.network.models

data class VoterInfo(
    val election: Election,
    val pollingLocations: String?,
    val contests: String?,
    val state: List<State>?,
    val electionElectionOfficials: List<Election>?
)