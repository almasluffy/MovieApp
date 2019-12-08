package com.example.kinopoisk.domain.repository

import com.example.kinopoisk.data.models.AccountData
import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface UserRepository {

    suspend fun createToken(): Response<JsonObject>

    suspend fun createSession(): Response<JsonObject>

    suspend fun login (username: String, password: String) : Boolean

    suspend fun getAccountDetails(sessionId: String): AccountData?
}