package co.digamma.ca.domain.api.food

import co.digamma.ca.domain.api.CrudService
import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import java.util.Locale

interface DishService : CrudService<Dish, DishCreation, DishModification> {
    fun retrieveSideDishes(dishId: String, pageSpecs: PageSpecs? = null): Page<Dish>
}

interface DishMutation {
    val name: String?
    val courseId: String?
    val cuisineId: String?
    val servingId: String?
    val imageIds: List<String>?
    val sideDishIds: List<String>?
}

data class DishCreation(
    val locale: Locale,
    override val name: String,
    override val courseId: String,
    override val cuisineId: String,
    override val servingId: String,
    override val imageIds: List<String> = emptyList(),
    override val sideDishIds: List<String> = emptyList(),
) : DishMutation


class DishModification(
    val id: String,
    override val name: String?,
    override val courseId: String?,
    override val cuisineId: String?,
    override val servingId: String?,
    override val imageIds: List<String>?,
    override val sideDishIds: List<String>?,
) : DishMutation
