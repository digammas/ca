package co.digamma.ca.domain.api.users

import co.digamma.ca.domain.api.DeleteService
import co.digamma.ca.domain.api.RetrieveService

interface UserService : RetrieveService<User>, DeleteService {
    fun create(user: UserCreation): User
    fun update(user: UserModification): User
}

interface UserMutation {
    val email: String?
    val password: String?
}

data class UserCreation(
    val username: String,
    override val email: String,
    override val password: String,
) : UserMutation

data class UserModification(
    val username: String,
    override val email: String?,
    override val password: String?,
) : UserMutation