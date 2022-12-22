package com.example.android.politicalpreparedness.network

import android.util.Log
import com.example.android.politicalpreparedness.base.Result
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteRepository private constructor() : RemoteDataSource {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    companion object {
        @Volatile
        private var INSTANCE: RemoteRepository? = null

        fun getInstance(): RemoteRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = RemoteRepository()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    private val civicService = CivicsApi.retrofitService

    override suspend fun getElections(): Result<List<Election>> = withContext(ioDispatcher) {
        return@withContext try {
            val electionResponse = civicService.getElections()
            val elections = electionResponse.elections.let {
                it.map { entity ->
                    entity
                }
            }
            Result.Success(elections)
        } catch (ex: Exception) {
            Log.d("getElections","Show ${ex.localizedMessage}")
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun getVoterInfo(address: String, electionId: Int): Result<VoterInfoResponse> =
        withContext(ioDispatcher) {
            return@withContext try {
                val voterInfoResponse = civicService.getVoterInfo(address, electionId)
                Result.Success(voterInfoResponse)
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
        }


    override suspend fun getRepresentatives(address: String): Result<RepresentativeResponse> =
        withContext(ioDispatcher) {
            return@withContext try {
                val representativeResponse = civicService.fetchRepresentatives(address)
                Result.Success(representativeResponse)
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
        }

}