package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.base.Result
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

interface RemoteDataSource {
    suspend fun getElections(): Result<List<Election>>

    suspend fun getVoterInfo(address: String, electionId: Int): Result<VoterInfoResponse>

    suspend fun getRepresentatives(address: String): Result<RepresentativeResponse>
}
