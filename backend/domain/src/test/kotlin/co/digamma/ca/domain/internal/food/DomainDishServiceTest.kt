package co.digamma.ca.domain.internal.food

import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.api.food.DishCreation
import co.digamma.ca.domain.api.food.DishModification
import co.digamma.ca.fixtures.givenLocale
import co.digamma.ca.fixtures.givenString
import co.digamma.ca.fixtures.inmem.food.InMemCourseRepository
import co.digamma.ca.fixtures.inmem.food.InMemCuisineRepository
import co.digamma.ca.fixtures.inmem.food.InMemDishRepository
import co.digamma.ca.fixtures.inmem.food.InMemServingRepository
import co.digamma.ca.fixtures.inmem.media.InMemImageRepository
import co.digamma.ca.suites.food.givenCourse
import co.digamma.ca.suites.food.givenCuisine
import co.digamma.ca.suites.food.givenDish
import co.digamma.ca.suites.food.givenServing
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DomainDishServiceTest {

    private val repository = InMemDishRepository()
    private val courseRepository = InMemCourseRepository()
    private val cuisineRepository = InMemCuisineRepository()
    private val servingRepository = InMemServingRepository()
    private val imageRepository = InMemImageRepository()
    private val sut = DomainDishService(
        repository,
        courseRepository,
        cuisineRepository,
        servingRepository,
        imageRepository,
    )

    private fun givenPersistedDish(): Dish = repository.create(givenDish())

    private fun createDish(sideDishIds: List<String> = emptyList()): Dish = sut.create(
        DishCreation(
            locale = givenLocale(),
            name = givenString(),
            courseId = courseRepository.create(givenCourse()).id,
            cuisineId = cuisineRepository.create(givenCuisine()).id,
            servingId = servingRepository.create(givenServing()).id,
            sideDishIds = sideDishIds,
        )
    )

    private fun dishModification(id: String, sideDishIds: List<String>?) = DishModification(
        id = id,
        name = null,
        courseId = null,
        cuisineId = null,
        servingId = null,
        imageIds = null,
        sideDishIds = sideDishIds,
    )

    @Test
    fun `test create with side dishes`() {
        val sideDish = givenPersistedDish()
        val dish = createDish(sideDishIds = listOf(sideDish.id))
        val sideDishes = sut.retrieveSideDishes(dish.id)
        assertEquals(listOf(sideDish.id), sideDishes.results.map { it.id })
    }

    @Test
    fun `test update replaces side dishes`() {
        val kept = givenPersistedDish()
        val removed = givenPersistedDish()
        val added = givenPersistedDish()
        val dish = createDish(sideDishIds = listOf(kept.id, removed.id))
        sut.update(dishModification(dish.id, sideDishIds = listOf(kept.id, added.id)))
        val sideDishes = sut.retrieveSideDishes(dish.id)
        assertEquals(setOf(kept.id, added.id), sideDishes.results.map { it.id }.toSet())
    }

    @Test
    fun `test update with null side dishes keeps existing`() {
        val sideDish = givenPersistedDish()
        val dish = createDish(sideDishIds = listOf(sideDish.id))
        sut.update(dishModification(dish.id, sideDishIds = null))
        val sideDishes = sut.retrieveSideDishes(dish.id)
        assertEquals(listOf(sideDish.id), sideDishes.results.map { it.id })
    }
}
