package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.controllers.graphql.PageQuery
import co.digamma.ca.controllers.graphql.asConnection
import co.digamma.ca.domain.api.food.CuisineService
import graphql.relay.Connection
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class CuisineController(
    private val service: CuisineService,
) {
    @QueryMapping
    fun cuisine(@Argument id: String): CuisineOutput {
        return service.retrieve(id).toCuisineOutput()
    }

    @QueryMapping
    fun cuisines(@Argument query: PageQuery): Connection<CuisineOutput> {
        return service.retrieve(query.asSpecs())
            .asConnection { it.toCuisineOutput() }
    }

    @MutationMapping
    fun deleteCuisine(@Argument id: String): Boolean {
        service.delete(id)
        return true
    }

    @MutationMapping
    fun createCuisine(@Argument creation: CuisineCreationInput): CuisineOutput {
        return service.create(creation.toCuisineCreation()).toCuisineOutput()
    }
}
