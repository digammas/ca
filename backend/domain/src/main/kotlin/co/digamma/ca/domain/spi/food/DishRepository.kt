package co.digamma.ca.domain.spi.food

import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.spi.CrudRepository

interface DishRepository: CrudRepository<Dish>