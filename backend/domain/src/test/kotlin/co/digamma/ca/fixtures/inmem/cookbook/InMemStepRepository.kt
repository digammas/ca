package co.digamma.ca.fixtures.inmem.cookbook

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.cookbook.Step
import co.digamma.ca.domain.spi.cookbook.StepRepository
import co.digamma.ca.fixtures.inmem.InMemCrudRepository

@Singleton
class InMemStepRepository : InMemCrudRepository<Step>(), StepRepository {
    override fun retrieveByRecipe(recipeId: String): List<Step> {
        return this.retrieveAll().filter { it.recipe.id == recipeId }
    }
}

