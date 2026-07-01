package co.digamma.ca.fixtures.inmem.cookbook

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.cookbook.Ingredient
import co.digamma.ca.domain.spi.cookbook.IngredientRepository
import co.digamma.ca.fixtures.inmem.InMemCrudRepository

@Singleton
class InMemIngredientRepository : InMemCrudRepository<Ingredient>(), IngredientRepository

