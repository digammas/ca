package co.digamma.ca.fixtures.inmem.food

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.common.NotFoundException
import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.fixtures.inmem.InMemCrudRepository

@Singleton
class InMemDishRepository : InMemCrudRepository<Dish>(), DishRepository {
    override fun retrieveSideDishes(dishId: String, pageSpecs: PageSpecs): Page<Dish> {
        val (index, size) = pageSpecs
        val dish = this.retrieve(dishId) ?: throw NotFoundException.of(dishId)
        val list = dish.sideDishes
            .stream()
            .skip(index * size.toLong())
            .limit(size.toLong())
            .toList()
        return Page(list, index, dish.sideDishes.size)
    }
}
