package co.digamma.ca.domain.spi.users

import co.digamma.ca.domain.api.users.User
import co.digamma.ca.domain.spi.CrudRepository

interface UserRepository : CrudRepository<User>