package co.digamma.ca.suites.food

import co.digamma.ca.domain.api.food.Course
import co.digamma.ca.domain.api.food.Cuisine
import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.api.food.Serving
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.domain.spi.food.CuisineRepository
import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.fixtures.utils.RandGen
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase
import java.util.Locale

abstract class DishRepositoryTestBase : CrudRepositoryTestBase<Dish>() {

    abstract override val sut: DishRepository
    abstract val courseRepository: CourseRepository
    abstract val cuisineRepository: CuisineRepository
    abstract val servingRepository: ServingRepository


    protected fun newCourse(): Course {
        val course = Course(
            id = RandGen.uuid(),
            locale = Locale.ENGLISH,
            name = RandGen.string()
        )
        return courseRepository.create(course)
    }

    protected fun newCuisine(): Cuisine {
        val cuisine = Cuisine(
            id = RandGen.uuid(),
            locale = Locale.ENGLISH,
            name = RandGen.string()
        )
        return cuisineRepository.create(cuisine)
    }

    protected fun newServing(): Serving {
        val serving = Serving(
            id = RandGen.uuid(),
            locale = Locale.ENGLISH,
            name = RandGen.string(),
            temperature = IntRange.EMPTY
        )
        return servingRepository.create(serving)
    }

    protected fun newDish(): Dish {
        val course = newCourse()
        val cuisine = newCuisine()
        val serving = newServing()
        val dish = Dish(
            id = RandGen.uuid(),
            locale = Locale.ENGLISH,
            name = RandGen.string(),
            course = course,
            cuisine = cuisine,
            serving = serving,
        )
        return dish
    }

    override fun newModel() = newDish()

    override fun modifyModel(model: Dish): Dish {
        val course = newCourse()
        val cuisine = newCuisine()
        val serving = newServing()
        return model.copy(
            locale = Locale.FRENCH,
            course = course,
            cuisine = cuisine,
            serving = serving,
        )
    }
}
