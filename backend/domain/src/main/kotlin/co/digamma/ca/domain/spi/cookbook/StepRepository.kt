package co.digamma.ca.domain.spi.cookbook

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.cookbook.Step
import co.digamma.ca.domain.spi.CrudRepository

interface StepRepository : CrudRepository<Step> {
    fun retrieveByRecipe(recipeId: String, pageSpecs: PageSpecs): Page<Step>
}
