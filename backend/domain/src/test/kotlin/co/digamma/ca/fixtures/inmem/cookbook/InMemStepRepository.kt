package co.digamma.ca.fixtures.inmem.cookbook

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.cookbook.Step
import co.digamma.ca.domain.spi.cookbook.StepRepository
import co.digamma.ca.fixtures.inmem.InMemCrudRepository

@Singleton
class InMemStepRepository : InMemCrudRepository<Step>(), StepRepository {
    override fun retrieveByRecipe(recipeId: String, pageSpecs: PageSpecs): Page<Step> {
        val (index, size) = pageSpecs
        val matches = this.retrieveAll().filter { it.recipe.id == recipeId }
        val list = matches
            .stream()
            .skip(index * size.toLong())
            .limit(size.toLong())
            .toList()
        return Page(list, index, matches.size)
    }
}

