package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.domain.api.cookbook.StepCreation


data class StepCreationInput(
    private val recipeId: String?,
    private val description: String,
    private val estimatedTime: Int,
    private val order: Int?,
) {

    fun toStepCreation() = StepCreation(
        recipeId = this.recipeId,
        description = this.description,
        estimatedTime = this.estimatedTime,
        order = this.order,
    )
}
