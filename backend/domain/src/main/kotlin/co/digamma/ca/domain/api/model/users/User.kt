package co.digamma.ca.domain.api.model.users

import co.digamma.ca.domain.api.model.Model

class User(
    val username: String,
    val password: String,
    val email: String,
): Model {

    override val id: String = username
}
