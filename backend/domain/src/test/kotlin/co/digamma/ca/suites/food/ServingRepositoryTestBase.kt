package co.digamma.ca.suites.food

import co.digamma.ca.domain.api.food.Serving
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.fixtures.utils.RandGen
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase
import java.util.Locale

abstract class ServingRepositoryTestBase : CrudRepositoryTestBase<Serving>() {

    abstract override val sut : ServingRepository

    override fun newModel() = Serving(
        id = RandGen.uuid(),
        locale = Locale.ENGLISH,
        name = RandGen.string(),
        temperature = IntRange.EMPTY
    )

    override fun modifyModel(model: Serving) = model.copy(
        locale = Locale.FRENCH,
        name = RandGen.string(),
        temperature = 25..30
    )
}
