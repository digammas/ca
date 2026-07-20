package co.digamma.ca.fixtures.inmem.food

import co.digamma.ca.domain.api.common.NotFoundException
import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.fixtures.inmem.InMemCrudRepository

@Singleton
class InMemDishRepository : InMemCrudRepository<Dish>(), DishRepository {
    override fun retrieveSideDishes(dishId: String): List<Dish> {
        val dish = this.retrieve(dishId) ?: throw NotFoundException.of(dishId)
        return dish.sideDishes
    }

    override fun countSideDishes(dishId: String): Int {
        val dish = this.retrieve(dishId) ?: throw NotFoundException.of(dishId)
        return dish.sideDishes.size
    }

    override fun countMainDishes(dishId: String): Int {
        this.retrieve(dishId) ?: throw NotFoundException.of(dishId)
        return this.retrieveAll().count { dish -> dish.sideDishes.any { it.id == dishId } }
    }
}
