package co.digamma.ca.fixtures.inmem.cookbook

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.cookbook.Recipe
import co.digamma.ca.domain.spi.cookbook.RecipeRepository
import co.digamma.ca.fixtures.inmem.InMemCrudRepository

@Singleton
class InMemRecipeRepository : InMemCrudRepository<Recipe>(), RecipeRepository

