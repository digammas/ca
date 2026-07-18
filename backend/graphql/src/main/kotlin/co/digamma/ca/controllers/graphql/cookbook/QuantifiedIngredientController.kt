package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.controllers.graphql.PageQuery
import co.digamma.ca.controllers.graphql.asConnection
import co.digamma.ca.domain.api.cookbook.QuantifiedIngredientService
import graphql.relay.Connection
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class QuantifiedIngredientController(
    private val service: QuantifiedIngredientService,
) {
    @QueryMapping
    fun quantifiedIngredient(@Argument id: String): QuantifiedIngredientOutput {
        return service.retrieve(id).toQuantifiedIngredientOutput()
    }

    @QueryMapping
    fun quantifiedIngredients(@Argument query: PageQuery?): Connection<QuantifiedIngredientOutput> {
        return service.retrieve(query?.asSpecs())
            .asConnection { it.toQuantifiedIngredientOutput() }
    }

    @MutationMapping
    fun deleteQuantifiedIngredient(@Argument id: String): Boolean {
        service.delete(id)
        return true
    }

    @MutationMapping
    fun createQuantifiedIngredient(@Argument creation: QuantifiedIngredientCreationInput): QuantifiedIngredientOutput {
        return service.create(creation.toQuantifiedIngredientCreation()).toQuantifiedIngredientOutput()
    }

    @MutationMapping
    fun updateQuantifiedIngredient(
        @Argument
        modification: QuantifiedIngredientModificationInput,
    ): QuantifiedIngredientOutput {
        return service.update(modification.toQuantifiedIngredientModification()).toQuantifiedIngredientOutput()
    }
}
