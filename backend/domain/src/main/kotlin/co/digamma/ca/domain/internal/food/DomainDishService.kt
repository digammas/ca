package co.digamma.ca.domain.internal.food

import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.api.food.DishCreation
import co.digamma.ca.domain.api.food.DishModification
import co.digamma.ca.domain.api.food.DishService
import co.digamma.ca.domain.api.media.Images
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.internal.retrieveOrThrow
import co.digamma.ca.domain.spi.CrudRepository
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.domain.spi.food.CuisineRepository
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.domain.spi.media.ImageRepository

class DomainDishService(
    override val repository: CrudRepository<Dish>,
    private val courseRepository: CourseRepository,
    private val cuisineRepository: CuisineRepository,
    private val servingRepository: ServingRepository,
    private val imageRepository: ImageRepository,
) : DefaultCurdService<Dish>(), DishService {

    override fun create(creation: DishCreation): Dish {
        val dish = Dish(
            id = generateId(),
            locale = creation.locale,
            name = creation.name,
            course = courseRepository.retrieveOrThrow(creation.courseId),
            cuisine = cuisineRepository.retrieveOrThrow(creation.cuisineId),
            serving = servingRepository.retrieveOrThrow(creation.servingId),
            images = Images(creation.imageIds.map(imageRepository::retrieveOrThrow)),
            sideDishes = creation.sideDishIds.map(repository::retrieveOrThrow),
        )
        return this.repository.create(dish)
    }

    override fun update(modification: DishModification): Dish {
        val existing = this.retrieve(modification.id)
        val dish = Dish(
            id = modification.id,
            locale = existing.locale,
            name = modification.name ?: existing.name,
            course = modification.courseId?.let(courseRepository::retrieveOrThrow) ?: existing.course,
            cuisine = modification.cuisineId?.let(cuisineRepository::retrieveOrThrow) ?: existing.cuisine,
            serving = modification.servingId?.let(servingRepository::retrieveOrThrow) ?: existing.serving,
            images = modification.imageIds?.map(imageRepository::retrieveOrThrow)?.let(::Images) ?: existing.images,
            sideDishes = modification.sideDishIds?.map(repository::retrieveOrThrow) ?: existing.sideDishes,
        )
        return repository.update(dish)
    }
}
