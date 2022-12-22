package com.example.android.politicalpreparedness.database

import android.content.Context
import android.util.Log
import com.example.android.politicalpreparedness.base.Result
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalRepository private constructor(context: Context, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : LocalDataSource {

    private val electionDao = ElectionDatabase.getInstance(context).electionDao

    companion object {
        @Volatile
        private var INSTANCE: LocalRepository? = null

        fun getInstance(context: Context, ioDispatcher: CoroutineDispatcher = Dispatchers.IO): LocalRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = LocalRepository(context, ioDispatcher)

                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    override suspend fun saveElection(election: Election): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            electionDao.saveElection(election)
            Result.Success(Unit)
        } catch (ex: Exception) {
            Log.d("saveElection","Show ${ex.localizedMessage}")
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun saveElections(entities: List<Election>): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            entities.forEach { item ->
                electionDao.saveElection(item)
            }
            Result.Success(Unit)
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun getElections(): Result<List<Election>> = withContext(ioDispatcher) {
        return@withContext try {
            val data = electionDao.getElections().map {
                it
            }
            Result.Success(data)
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun getElection(id: Int): Result<Election> = withContext(ioDispatcher) {
        return@withContext try {
            val election = electionDao.getElection(id)
            if (election != null) {
                Result.Success(election)
            } else {
                Result.Error("Election not found!")
            }
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun deleteElection(entity: Election): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            electionDao.deleteElection(entity)
            Result.Success(Unit)
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun clearElections(): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            electionDao.clearElections()
            Result.Success(Unit)
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

}