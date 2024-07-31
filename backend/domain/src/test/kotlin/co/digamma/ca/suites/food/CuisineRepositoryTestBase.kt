package co.digamma.ca.suites.food

import co.digamma.ca.domain.api.food.Cuisine
import co.digamma.ca.domain.spi.food.CuisineRepository
import co.digamma.ca.fixtures.utils.RandGen
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase
import java.util.Locale

abstract class CuisineRepositoryTestBase : CrudRepositoryTestBase<Cuisine>() {

    abstract override val sut : CuisineRepository

    override fun newModel() = Cuisine(
        id = RandGen.uuid(),
        locale = Locale.ENGLISH,
        name = RandGen.string()
    )

    override fun modifyModel(model: Cuisine) = model.copy(
        locale = Locale.FRENCH,
        name = RandGen.string()
    )
}
