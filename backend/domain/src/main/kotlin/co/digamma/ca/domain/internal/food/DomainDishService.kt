package co.digamma.ca.domain.internal.food

import co.digamma.ca.domain.api.common.BadInput
import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.api.food.DishCreation
import co.digamma.ca.domain.api.food.DishModification
import co.digamma.ca.domain.api.food.DishService
import co.digamma.ca.domain.api.media.Images
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.domain.spi.food.CuisineRepository
import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.domain.spi.media.ImageRepository

@Singleton
open class DomainDishService(
    override val repository: DishRepository,
    private val courseRepository: CourseRepository,
    private val cuisineRepository: CuisineRepository,
    private val servingRepository: ServingRepository,
    private val imageRepository: ImageRepository,
) : DefaultCurdService<Dish, DishCreation, DishModification>(), DishService {

    override fun create(creation: DishCreation): Dish {
        validateSideDishIds(null, creation.sideDishIds)
        return super.create(creation)
    }

    override fun update(modification: DishModification): Dish {
        modification.sideDishIds?.let { validateSideDishIds(modification.id, it) }
        return super.update(modification)
    }

    override fun toModel(creation: DishCreation) = Dish(
        id = generateId(),
        locale = creation.locale,
        name = creation.name,
        course = courseRepository.retrieveOrThrow(creation.courseId),
        cuisine = cuisineRepository.retrieveOrThrow(creation.cuisineId),
        serving = servingRepository.retrieveOrThrow(creation.servingId),
        images = Images(creation.imageIds.map { imageRepository.retrieveOrThrow(it) }),
        sideDishes = creation.sideDishIds.map { repository.retrieveOrThrow(it) },
    )

    override fun toModel(modification: DishModification, existing: Dish) = Dish(
        id = modification.id,
        locale = existing.locale,
        name = modification.name ?: existing.name,
        course = modification.courseId?.let { courseRepository.retrieveOrThrow(it) } ?: existing.course,
        cuisine = modification.cuisineId?.let { cuisineRepository.retrieveOrThrow(it) } ?: existing.cuisine,
        serving = modification.servingId?.let { servingRepository.retrieveOrThrow(it) } ?: existing.serving,
        images = modification.imageIds
            ?.map { imageRepository.retrieveOrThrow(it) }
            ?.let(::Images)
            ?: existing.images,
        sideDishes = modification.sideDishIds
            ?.map { repository.retrieveOrThrow(it) }
            ?: existing.sideDishes,
    )

    override fun retrieveSideDishes(dishId: String): List<Dish> {
        return this.repository.retrieveSideDishes(dishId)
    }

    private fun validateSideDishIds(dishId: String?, sideDishIds: List<String>) {
        if (sideDishIds.isEmpty()) {
            return
        }
        if (dishId != null && this.repository.countMainDishes(dishId) > 0) {
            throw BadInput("Dish $dishId is already a side dish and cannot have side dishes of its own.")
        }
        if (sideDishIds.any { it == dishId }) {
            throw BadInput("A dish cannot be one of its own side dishes.")
        }
        sideDishIds.find {
            this.repository.countSideDishes(it) > 0
        }?.let {
            throw BadInput("Dish $it already has side dishes and cannot be used as a side dish.")
        }
    }
}
