package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.controllers.graphql.PageQuery
import co.digamma.ca.controllers.graphql.asConnection
import co.digamma.ca.domain.api.food.ServingService
import graphql.relay.Connection
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class ServingController(
    private val service: ServingService,
) {
    @QueryMapping
    fun serving(@Argument id: String): ServingOutput {
        return service.retrieve(id).toServingOutput()
    }

    @QueryMapping
    fun servings(@Argument query: PageQuery): Connection<ServingOutput> {
        return service.retrieve(query.asSpecs())
            .asConnection { it.toServingOutput() }
    }

    @MutationMapping
    fun deleteServing(@Argument id: String): Boolean {
        service.delete(id)
        return true
    }

    @MutationMapping
    fun createServing(@Argument creation: ServingCreationInput): ServingOutput {
        return service.create(creation.toServingCreation()).toServingOutput()
    }
}
