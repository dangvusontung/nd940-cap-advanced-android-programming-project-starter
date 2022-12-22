package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.base.Result
import com.example.android.politicalpreparedness.network.models.Election

interface LocalDataSource {
    suspend fun saveElection(election: Election): Result<Unit>

    suspend fun saveElections(entities: List<Election>): Result<Unit>

    suspend fun getElections(): Result<List<Election>>

    suspend fun getElection(id: Int): Result<Election>

    suspend fun deleteElection(entity: Election): Result<Unit>

    suspend fun clearElections(): Result<Unit>
}