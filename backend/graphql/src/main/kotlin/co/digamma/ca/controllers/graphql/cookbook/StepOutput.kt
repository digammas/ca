package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.domain.api.cookbook.Step

data class StepOutput(
    val id: String,
    val locale: String,
    val description: String,
    val estimatedTime: Int,
    val recipe: RecipeOutput,
)

fun Step.toStepOutput() = StepOutput(
    id = this.id,
    locale = this.locale.toString(),
    description = this.description,
    estimatedTime = this.estimatedTime,
    recipe = this.recipe.toRecipeOutput(),
)
