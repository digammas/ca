package co.digamma.ca.domain.spi.food

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.spi.CrudRepository

interface DishRepository : CrudRepository<Dish> {
    fun retrieveSideDishes(dishId: String, pageSpecs: PageSpecs): Page<Dish>
}
