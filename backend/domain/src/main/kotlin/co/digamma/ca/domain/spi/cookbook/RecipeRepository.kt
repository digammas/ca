package co.digamma.ca.domain.spi.cookbook

import co.digamma.ca.domain.api.cookbook.Recipe
import co.digamma.ca.domain.spi.CrudRepository

interface RecipeRepository : CrudRepository<Recipe>
