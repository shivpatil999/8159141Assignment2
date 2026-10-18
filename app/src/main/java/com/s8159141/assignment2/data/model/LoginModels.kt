package com.s8159141.assignment2.data.model

/** Body of POST /{campus}/auth. */
data class LoginRequest(
    val username: String,
    val password: String
)

/** Successful auth response; [keypass] is the topic name used for the dashboard call. */
data class LoginResponse(
    val keypass: String
)
