package com.s8159141.assignment2.data.repository

import com.s8159141.assignment2.data.model.Entity
import com.s8159141.assignment2.data.remote.ApiService
import com.s8159141.assignment2.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

/** Repository interface for the dashboard data. */
interface DashboardRepository {
    /** Fetches the list of entities for the given keypass (topic). */
    suspend fun getEntities(keypass: String): Result<List<Entity>>
}

class DashboardRepositoryImpl @Inject constructor(
    private val api: ApiService,
    @Named("io") private val ioDispatcher: CoroutineDispatcher
) : DashboardRepository {

    override suspend fun getEntities(keypass: String): Result<List<Entity>> =
        withContext(ioDispatcher) {
            try {
                val response = api.getDashboard(keypass)
                val body = response.body()
                when {
                    response.isSuccessful && body != null -> Result.Success(body.entities)
                    response.code() == 404 -> Result.Error("No data found for this topic.")
                    else -> Result.Error("Could not load data (error ${response.code()}).")
                }
            } catch (e: IOException) {
                Result.Error("No internet connection. Please check your network and try again.")
            } catch (e: Exception) {
                Result.Error("Something went wrong while loading data.")
            }
        }
}
