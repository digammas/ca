package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.domain.api.cookbook.StepModification


data class StepModificationInput(
    private val id: String,
    private val description: String?,
    private val estimatedTime: Int?,
    private val order: Int?,
) {

    fun toStepModification() = StepModification(
        id = this.id,
        description = this.description,
        estimatedTime = this.estimatedTime,
        order = this.order,
    )
}
