package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.controllers.graphql.PageQuery
import co.digamma.ca.controllers.graphql.asConnection
import co.digamma.ca.domain.api.food.DishService
import graphql.relay.Connection
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class DishController(
    private val service: DishService,
) {
    @QueryMapping
    fun dish(@Argument id: String): DishOutput {
        return service.retrieve(id).toDishOutput()
    }

    @QueryMapping
    fun dishes(@Argument query: PageQuery?): Connection<DishOutput> {
        return service.retrieve(query?.asSpecs())
            .asConnection { it.toDishOutput() }
    }

    @MutationMapping
    fun deleteDish(@Argument id: String): Boolean {
        service.delete(id)
        return true
    }

    @MutationMapping
    fun createDish(@Argument creation: DishCreationInput): DishOutput {
        return service.create(creation.toDishCreation()).toDishOutput()
    }

    @SchemaMapping
    fun sideDishes(dish: DishOutput): List<DishOutput> {
        return this.service.retrieveSideDishes(dish.id).results
            .map { it.toDishOutput() }
    }
}
