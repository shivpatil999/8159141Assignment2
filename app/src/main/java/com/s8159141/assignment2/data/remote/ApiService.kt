package com.s8159141.assignment2.data.remote

import com.s8159141.assignment2.data.model.DashboardResponse
import com.s8159141.assignment2.data.model.LoginRequest
import com.s8159141.assignment2.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit description of the nit3213 API.
 * Responses are wrapped in [Response] so the repository can read HTTP status codes
 * (e.g. 400 wrong password, 404 unknown user) and turn them into user-facing messages.
 */
interface ApiService {

    @POST("{campus}/auth")
    suspend fun login(
        @Path("campus") campus: String,
        @Body body: LoginRequest
    ): Response<LoginResponse>

    @GET("dashboard/{keypass}")
    suspend fun getDashboard(
        @Path("keypass") keypass: String
    ): Response<DashboardResponse>
}
