package co.digamma.ca.domain.spi.cookbook

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.cookbook.QuantifiedIngredient
import co.digamma.ca.domain.spi.CrudRepository

interface QuantifiedIngredientRepository : CrudRepository<QuantifiedIngredient> {
    fun retrieveByRecipe(recipeId: String, pageSpecs: PageSpecs): Page<QuantifiedIngredient>
}
