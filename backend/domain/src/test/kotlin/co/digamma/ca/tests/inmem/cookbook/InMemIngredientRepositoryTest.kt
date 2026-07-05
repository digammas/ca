package co.digamma.ca.tests.inmem.cookbook

import co.digamma.ca.fixtures.inmem.cookbook.InMemIngredientRepository
import co.digamma.ca.suites.cookbook.IngredientRepositoryTestBase

class InMemIngredientRepositoryTest : IngredientRepositoryTestBase() {
    override val sut = InMemIngredientRepository()
}

