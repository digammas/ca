package co.digamma.ca.tests.inmem.cookbook

import co.digamma.ca.fixtures.inmem.cookbook.InMemRecipeRepository
import co.digamma.ca.fixtures.inmem.cookbook.InMemStepRepository
import co.digamma.ca.fixtures.inmem.food.InMemCourseRepository
import co.digamma.ca.fixtures.inmem.food.InMemCuisineRepository
import co.digamma.ca.fixtures.inmem.food.InMemDishRepository
import co.digamma.ca.fixtures.inmem.food.InMemServingRepository
import co.digamma.ca.fixtures.inmem.users.InMemUserRepository
import co.digamma.ca.suites.cookbook.StepRepositoryTestBase

class InMemStepRepositoryTest : StepRepositoryTestBase() {
    override val sut = InMemStepRepository()
    override val recipeRepository = InMemRecipeRepository()
    override val dishRepository = InMemDishRepository()
    override val courseRepository = InMemCourseRepository()
    override val cuisineRepository = InMemCuisineRepository()
    override val servingRepository = InMemServingRepository()
    override val userRepository = InMemUserRepository()
}

