package co.digamma.ca.domain.spi.food

import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.spi.CrudRepository

interface DishRepository : CrudRepository<Dish> {
    fun retrieveSideDishes(dishId: String): List<Dish>
    fun countSideDishes(dishId: String): Int
    fun countMainDishes(dishId: String): Int
}
