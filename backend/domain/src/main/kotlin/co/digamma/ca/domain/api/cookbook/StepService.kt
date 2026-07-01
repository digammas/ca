package co.digamma.ca.domain.api.cookbook

import co.digamma.ca.domain.api.CrudService

interface StepService: CrudService<Step, StepCreation, StepUpdate>

interface StepMutation {
    val description: String?
    val estimatedTime: Int?
    val order: Int?
}

data class StepCreation(
    val recipeId: String? = null,
    override val description: String,
    override val estimatedTime: Int,
    override val order: Int? = null,
) : StepMutation

data class StepUpdate(
    val id: String,
    override val description: String? = null,
    override val estimatedTime: Int? = null,
    override val order: Int? = null,
) : StepMutation