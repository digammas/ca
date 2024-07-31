package co.digamma.ca.tests.inmem.food

import co.digamma.ca.fixtures.inmem.food.InMemCuisineRepository
import co.digamma.ca.suites.food.CuisineRepositoryTestBase

class InMemCuisineRepositoryTest : CuisineRepositoryTestBase() {
    override val sut = InMemCuisineRepository()
}