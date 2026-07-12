package co.digamma.ca.suites.food

import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.domain.spi.food.CuisineRepository
import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.fixtures.givenLocale
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase

abstract class DishRepositoryTestBase : CrudRepositoryTestBase<Dish>() {

    abstract override val sut: DishRepository
    abstract val courseRepository: CourseRepository
    abstract val cuisineRepository: CuisineRepository
    abstract val servingRepository: ServingRepository

    override fun newModel() = givenDish().also {
        courseRepository.create(it.course)
        cuisineRepository.create(it.cuisine)
        servingRepository.create(it.serving)
    }

    override fun modifyModel(model: Dish) = model.copy(
        locale = givenLocale(),
        course = givenCourse().also(courseRepository::create),
        cuisine = givenCuisine().also(cuisineRepository::create),
        serving = givenServing().also(servingRepository::create),
    )
}
