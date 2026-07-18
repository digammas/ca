package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.controllers.graphql.PageQuery
import co.digamma.ca.controllers.graphql.asConnection
import co.digamma.ca.domain.api.cookbook.IngredientService
import graphql.relay.Connection
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class IngredientController(
    private val service: IngredientService,
) {
    @QueryMapping
    fun ingredient(@Argument id: String): IngredientOutput {
        return service.retrieve(id).toIngredientOutput()
    }

    @QueryMapping
    fun ingredients(@Argument query: PageQuery?): Connection<IngredientOutput> {
        return service.retrieve(query?.asSpecs())
            .asConnection { it.toIngredientOutput() }
    }

    @MutationMapping
    fun deleteIngredient(@Argument id: String): Boolean {
        service.delete(id)
        return true
    }

    @MutationMapping
    fun createIngredient(@Argument creation: IngredientCreationInput): IngredientOutput {
        return service.create(creation.toIngredientCreation()).toIngredientOutput()
    }

    @MutationMapping
    fun updateIngredient(@Argument modification: IngredientModificationInput): IngredientOutput {
        return service.update(modification.toIngredientModification()).toIngredientOutput()
    }
}
