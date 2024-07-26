package co.digamma.ca.tests.inmem.food

import co.digamma.ca.fixtures.inmem.food.InMemCourseRepository
import co.digamma.ca.fixtures.inmem.food.InMemCuisineRepository
import co.digamma.ca.fixtures.inmem.food.InMemDishRepository
import co.digamma.ca.fixtures.inmem.food.InMemServingRepository
import co.digamma.ca.suites.food.DishRepositoryTestBase

class InMemDishRepositoryTest : DishRepositoryTestBase() {

    override val sut = InMemDishRepository()
    override val courseRepository = InMemCourseRepository()
    override val cuisineRepository = InMemCuisineRepository()
    override val servingRepository = InMemServingRepository()
}