package co.digamma.ca.suites.food

import co.digamma.ca.domain.api.food.Cuisine
import co.digamma.ca.domain.spi.food.CuisineRepository
import co.digamma.ca.fixtures.givenLocale
import co.digamma.ca.fixtures.givenString
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase

abstract class CuisineRepositoryTestBase : CrudRepositoryTestBase<Cuisine>() {

    abstract override val sut: CuisineRepository

    override fun newModel() = givenCuisine()

    override fun modifyModel(model: Cuisine) = model.copy(
        locale = givenLocale(),
        name = givenString()
    )
}
