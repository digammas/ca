package co.digamma.ca.suites.cookbook

import co.digamma.ca.domain.api.cookbook.Ingredient
import co.digamma.ca.domain.spi.cookbook.IngredientRepository
import co.digamma.ca.fixtures.utils.RandGen
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase
import java.util.Locale

abstract class IngredientRepositoryTestBase : CrudRepositoryTestBase<Ingredient>() {

    abstract override val sut: IngredientRepository

    override fun newModel() = Ingredient(
        id = RandGen.uuid(),
        locale = Locale.ENGLISH,
        name = RandGen.string(),
        description = RandGen.string(),
    )

    override fun modifyModel(model: Ingredient) = Ingredient(
        id = model.id,
        locale = Locale.FRENCH,
        name = RandGen.string(),
        description = RandGen.string(),
        images = model.images,
    )
}

