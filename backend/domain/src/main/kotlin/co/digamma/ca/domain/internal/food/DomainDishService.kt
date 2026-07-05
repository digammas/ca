package co.digamma.ca.domain.internal.food

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
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

    override fun toModel(creation: DishCreation) = Dish(
        id = generateId(),
        locale = creation.locale,
        name = creation.name,
        course = courseRepository.retrieveOrThrow(creation.courseId),
        cuisine = cuisineRepository.retrieveOrThrow(creation.cuisineId),
        serving = servingRepository.retrieveOrThrow(creation.servingId),
        images = Images(creation.imageIds.map { imageRepository.retrieveOrThrow(it) }),
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
        )

    override fun retrieveSideDishes(dishId: String, pageSpecs: PageSpecs?): Page<Dish> {
        return this.repository.retrieveSideDishes(dishId, pageSpecs ?: this.defaultPageSpecs)
    }
}
