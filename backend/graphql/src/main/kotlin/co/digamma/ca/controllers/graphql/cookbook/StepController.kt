package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.controllers.graphql.PageQuery
import co.digamma.ca.controllers.graphql.asConnection
import co.digamma.ca.domain.api.cookbook.StepService
import graphql.relay.Connection
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class StepController(
    private val service: StepService,
) {
    @QueryMapping
    fun step(@Argument id: String): StepOutput {
        return service.retrieve(id).toStepOutput()
    }

    @QueryMapping
    fun steps(@Argument query: PageQuery?): Connection<StepOutput> {
        return service.retrieve(query?.asSpecs())
            .asConnection { it.toStepOutput() }
    }

    @MutationMapping
    fun deleteStep(@Argument id: String): Boolean {
        service.delete(id)
        return true
    }

    @MutationMapping
    fun createStep(@Argument creation: StepCreationInput): StepOutput {
        return service.create(creation.toStepCreation()).toStepOutput()
    }

    @MutationMapping
    fun updateStep(@Argument modification: StepModificationInput): StepOutput {
        return service.update(modification.toStepModification()).toStepOutput()
    }
}
