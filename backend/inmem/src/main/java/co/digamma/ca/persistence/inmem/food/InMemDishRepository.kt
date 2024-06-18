package co.digamma.ca.persistence.inmem.food

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.persistence.inmem.InMemCrudRepository

@Singleton
class InMemDishRepository: InMemCrudRepository<Dish>(), DishRepository