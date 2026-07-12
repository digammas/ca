package co.digamma.ca.suites.cookbook

import co.digamma.ca.domain.api.cookbook.Ingredient
import co.digamma.ca.domain.spi.cookbook.IngredientRepository
import co.digamma.ca.fixtures.utils.cookbook.givenIngredient
import co.digamma.ca.fixtures.utils.givenLocale
import co.digamma.ca.fixtures.utils.givenString
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase

abstract class IngredientRepositoryTestBase : CrudRepositoryTestBase<Ingredient>() {

    abstract override val sut: IngredientRepository

    override fun newModel() = givenIngredient()

    override fun modifyModel(model: Ingredient) = Ingredient(
        id = model.id,
        locale = givenLocale(),
        name = givenString(),
        description = givenString(),
        images = model.images,
    )
}

