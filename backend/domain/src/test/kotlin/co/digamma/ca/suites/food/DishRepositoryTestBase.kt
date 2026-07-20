package co.digamma.ca.suites.food

import co.digamma.ca.domain.api.common.NotFoundException
import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.domain.spi.food.CuisineRepository
import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.fixtures.givenLocale
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

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

    private fun newSideDish(): Dish = sut.create(newModel())

    @Test
    fun `test create with side dishes`() {
        val sideDish = newSideDish()
        val dish = sut.create(newModel().copy(sideDishes = listOf(sideDish)))
        val retrieved = sut.retrieveSideDishes(dish.id)
        assertEquals(listOf(sideDish.id), retrieved.map { it.id })
    }

    @Test
    fun `test retrieve side dishes`() {
        val first = newSideDish()
        val second = newSideDish()
        val dish = sut.create(newModel().copy(sideDishes = listOf(first, second)))
        val retrieved = sut.retrieveSideDishes(dish.id)
        assertEquals(setOf(first.id, second.id), retrieved.map { it.id }.toSet())
    }

    @Test
    fun `test retrieve side dishes empty`() {
        val dish = sut.create(newModel())
        val retrieved = sut.retrieveSideDishes(dish.id)
        assertTrue(retrieved.isEmpty())
    }

    @Test
    fun `test retrieve side dishes not found`() {
        assertThrows(NotFoundException::class.java) {
            sut.retrieveSideDishes("not-found")
        }
    }

    @Test
    fun `test update replaces side dishes`() {
        val kept = newSideDish()
        val removed = newSideDish()
        val added = newSideDish()
        val dish = sut.create(newModel().copy(sideDishes = listOf(kept, removed)))
        sut.update(dish.copy(sideDishes = listOf(kept, added)))
        val retrieved = sut.retrieveSideDishes(dish.id)
        assertEquals(setOf(kept.id, added.id), retrieved.map { it.id }.toSet())
    }

    @Test
    fun `test count side dishes`() {
        val first = newSideDish()
        val second = newSideDish()
        val dish = sut.create(newModel().copy(sideDishes = listOf(first, second)))
        assertEquals(2, sut.countSideDishes(dish.id))
    }

    @Test
    fun `test count side dishes empty`() {
        val dish = sut.create(newModel())
        assertEquals(0, sut.countSideDishes(dish.id))
    }

    @Test
    fun `test count side dishes not found`() {
        assertThrows(NotFoundException::class.java) {
            sut.countSideDishes("not-found")
        }
    }

    @Test
    fun `test count main dishes`() {
        val sideDish = newSideDish()
        sut.create(newModel().copy(sideDishes = listOf(sideDish)))
        sut.create(newModel().copy(sideDishes = listOf(sideDish)))
        assertEquals(2, sut.countMainDishes(sideDish.id))
    }

    @Test
    fun `test count main dishes empty`() {
        val dish = sut.create(newModel())
        assertEquals(0, sut.countMainDishes(dish.id))
    }

    @Test
    fun `test count main dishes not found`() {
        assertThrows(NotFoundException::class.java) {
            sut.countMainDishes("not-found")
        }
    }
}
