package co.digamma.ca.domain.internal.cookbook

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.cookbook.Step
import co.digamma.ca.domain.api.cookbook.StepCreation
import co.digamma.ca.domain.api.cookbook.StepService
import co.digamma.ca.domain.api.cookbook.StepModification
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.spi.cookbook.RecipeRepository
import co.digamma.ca.domain.spi.cookbook.StepRepository

@Singleton
open class DomainStepService(
    override val repository: StepRepository,
    private val recipeRepository: RecipeRepository,
) : DefaultCurdService<Step, StepCreation, StepModification>(), StepService {

    override fun toModel(creation: StepCreation): Step {
        val recipeId = creation.recipeId ?: throw IllegalArgumentException("recipeId is required")
        val recipe = recipeRepository.retrieveOrThrow(recipeId)
        return Step(
            id = generateId(),
            locale = recipe.locale,
            description = creation.description,
            estimatedTime = creation.estimatedTime,
            recipe = recipe,
        )
    }

    override fun toModel(modification: StepModification, existing: Step) = Step(
        id = existing.id,
        locale = existing.locale,
        description = modification.description ?: existing.description,
        estimatedTime = modification.estimatedTime ?: existing.estimatedTime,
        recipe = existing.recipe,
    )
}

