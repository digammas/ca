package co.digamma.ca.fixtures.inmem.users

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.users.User
import co.digamma.ca.domain.spi.users.UserRepository
import co.digamma.ca.fixtures.inmem.InMemCrudRepository

@Singleton
class InMemUserRepository : InMemCrudRepository<User>(), UserRepository

