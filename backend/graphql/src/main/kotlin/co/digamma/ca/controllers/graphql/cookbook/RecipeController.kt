package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.controllers.graphql.PageQuery
import co.digamma.ca.controllers.graphql.asConnection
import co.digamma.ca.domain.api.cookbook.QuantifiedIngredientService
import co.digamma.ca.domain.api.cookbook.RecipeService
import co.digamma.ca.domain.api.cookbook.StepService
import graphql.relay.Connection
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class RecipeController(
    private val service: RecipeService,
    private val stepService: StepService,
    private val quantifiedIngredientService: QuantifiedIngredientService,
) {
    @QueryMapping
    fun recipe(@Argument id: String): RecipeOutput {
        return service.retrieve(id).toRecipeOutput()
    }

    @QueryMapping
    fun recipes(@Argument query: PageQuery?): Connection<RecipeOutput> {
        return service.retrieve(query?.asSpecs())
            .asConnection { it.toRecipeOutput() }
    }

    @MutationMapping
    fun deleteRecipe(@Argument id: String): Boolean {
        service.delete(id)
        return true
    }

    @MutationMapping
    fun createRecipe(@Argument creation: RecipeCreationInput): RecipeOutput {
        return service.create(creation.toRecipeCreation()).toRecipeOutput()
    }

    @MutationMapping
    fun updateRecipe(@Argument modification: RecipeModificationInput): RecipeOutput {
        return service.update(modification.toRecipeModification()).toRecipeOutput()
    }

    @SchemaMapping
    fun steps(recipe: RecipeOutput): List<StepOutput> {
        return this.stepService.retrieveByRecipe(recipe.id)
            .map { it.toStepOutput() }
    }

    @SchemaMapping
    fun ingredients(recipe: RecipeOutput): List<QuantifiedIngredientOutput> {
        return this.quantifiedIngredientService.retrieveByRecipe(recipe.id)
            .map { it.toQuantifiedIngredientOutput() }
    }
}
