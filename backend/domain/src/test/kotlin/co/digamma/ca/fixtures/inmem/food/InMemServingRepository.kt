package co.digamma.ca.fixtures.inmem.food

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.food.Serving
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.fixtures.inmem.InMemCrudRepository

@Singleton
class InMemServingRepository : InMemCrudRepository<Serving>(), ServingRepository