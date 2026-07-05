package co.digamma.ca.domain.api.users

import co.digamma.ca.domain.api.CrudService

interface UserService : CrudService<User, UserCreation, UserModification>

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