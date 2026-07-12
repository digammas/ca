package co.digamma.ca.suites.food

import co.digamma.ca.domain.api.food.Serving
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.fixtures.givenLocale
import co.digamma.ca.fixtures.givenString
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase

abstract class ServingRepositoryTestBase : CrudRepositoryTestBase<Serving>() {

    abstract override val sut: ServingRepository

    override fun newModel() = givenServing()

    override fun modifyModel(model: Serving) = model.copy(
        locale = givenLocale(),
        name = givenString(),
    )
}
