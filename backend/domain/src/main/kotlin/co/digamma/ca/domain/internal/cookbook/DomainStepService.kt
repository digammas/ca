package co.digamma.ca.domain.internal.cookbook

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.cookbook.Step
import co.digamma.ca.domain.api.cookbook.StepCreation
import co.digamma.ca.domain.api.cookbook.StepService
import co.digamma.ca.domain.api.cookbook.StepUpdate
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.internal.retrieveOrThrow
import co.digamma.ca.domain.spi.cookbook.RecipeRepository
import co.digamma.ca.domain.spi.cookbook.StepRepository

@Singleton
open class DomainStepService(
    override val repository: StepRepository,
    private val recipeRepository: RecipeRepository,
) : DefaultCurdService<Step>(), StepService {

    override fun create(creation: StepCreation): Step {
        val recipeId = creation.recipeId ?: throw IllegalArgumentException("recipeId is required")
        val recipe = recipeRepository.retrieveOrThrow(recipeId)
        val model = Step(
            id = generateId(),
            locale = recipe.locale,
            description = creation.description,
            estimatedTime = creation.estimatedTime,
            recipe = recipe,
        )
        return repository.create(model)
    }

    override fun update(modification: StepUpdate): Step {
        val existing = this.retrieve(modification.id)
        val model = Step(
            id = existing.id,
            locale = existing.locale,
            description = modification.description ?: existing.description,
            estimatedTime = modification.estimatedTime ?: existing.estimatedTime,
            recipe = existing.recipe,
        )
        return repository.update(model)
    }
}

