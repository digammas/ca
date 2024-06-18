package co.digamma.ca.persistence.inmem.food

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.food.Serving
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.persistence.inmem.InMemCrudRepository

@Singleton
class InMemServingRepository: InMemCrudRepository<Serving>(), ServingRepository