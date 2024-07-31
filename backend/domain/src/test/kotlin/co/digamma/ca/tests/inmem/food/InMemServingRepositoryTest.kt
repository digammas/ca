package co.digamma.ca.tests.inmem.food

import co.digamma.ca.fixtures.inmem.food.InMemServingRepository
import co.digamma.ca.suites.food.ServingRepositoryTestBase

class InMemServingRepositoryTest : ServingRepositoryTestBase() {
    override val sut = InMemServingRepository()
}