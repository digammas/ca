package co.digamma.ca.domain.spi.cookbook

import co.digamma.ca.domain.api.cookbook.Ingredient
import co.digamma.ca.domain.spi.CrudRepository

interface IngredientRepository: CrudRepository<Ingredient>
