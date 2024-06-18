package co.digamma.ca.persistence.inmem.food

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.food.Cuisine
import co.digamma.ca.domain.spi.food.CuisineRepository
import co.digamma.ca.persistence.inmem.InMemCrudRepository

@Singleton
class InMemCuisineRepository: InMemCrudRepository<Cuisine>(), CuisineRepository