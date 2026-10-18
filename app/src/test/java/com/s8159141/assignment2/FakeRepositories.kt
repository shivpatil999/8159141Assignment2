package com.s8159141.assignment2

import com.s8159141.assignment2.data.model.Entity
import com.s8159141.assignment2.data.repository.AuthRepository
import com.s8159141.assignment2.data.repository.DashboardRepository
import com.s8159141.assignment2.util.Result

/** Fake auth repository returning a preset result, for ViewModel tests. */
class FakeAuthRepository(
    var result: Result<String> = Result.Success("architecture")
) : AuthRepository {
    var lastUsername: String? = null
    var lastPassword: String? = null
    override suspend fun login(username: String, password: String): Result<String> {
        lastUsername = username
        lastPassword = password
        return result
    }
}

/** Fake dashboard repository returning a preset result. */
class FakeDashboardRepository(
    var result: Result<List<Entity>> = Result.Success(emptyList())
) : DashboardRepository {
    var lastKeypass: String? = null
    override suspend fun getEntities(keypass: String): Result<List<Entity>> {
        lastKeypass = keypass
        return result
    }
}

/** Shared sample entity for tests. */
fun sampleEntity(name: String = "Eiffel Tower") = Entity(
    name = name,
    architect = "Gustave Eiffel",
    location = "Paris, France",
    yearCompleted = 1889,
    style = "Structural Expressionism",
    height = 324,
    description = "An iron lattice tower on the Champ de Mars in Paris."
)
