package co.digamma.ca.domain.internal

import co.digamma.ca.domain.api.users.User
import co.digamma.ca.domain.api.users.UserService
import co.digamma.ca.domain.spi.users.UserRepository

class DefaultUserService(
    override val repository: UserRepository,
): DefaultCurdService<User>(), UserService