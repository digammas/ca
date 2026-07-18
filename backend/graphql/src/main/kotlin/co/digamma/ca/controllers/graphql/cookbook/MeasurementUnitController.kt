package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.controllers.graphql.PageQuery
import co.digamma.ca.controllers.graphql.asConnection
import co.digamma.ca.domain.api.cookbook.MeasurementUnitService
import graphql.relay.Connection
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class MeasurementUnitController(
    private val service: MeasurementUnitService,
) {
    @QueryMapping
    fun measurementUnit(@Argument id: String): MeasurementUnitOutput {
        return service.retrieve(id).toMeasurementUnitOutput()
    }

    @QueryMapping
    fun measurementUnits(@Argument query: PageQuery?): Connection<MeasurementUnitOutput> {
        return service.retrieve(query?.asSpecs())
            .asConnection { it.toMeasurementUnitOutput() }
    }

    @MutationMapping
    fun deleteMeasurementUnit(@Argument id: String): Boolean {
        service.delete(id)
        return true
    }

    @MutationMapping
    fun createMeasurementUnit(@Argument creation: MeasurementUnitCreationInput): MeasurementUnitOutput {
        return service.create(creation.toMeasurementUnitCreation()).toMeasurementUnitOutput()
    }

    @MutationMapping
    fun updateMeasurementUnit(@Argument modification: MeasurementUnitModificationInput): MeasurementUnitOutput {
        return service.update(modification.toMeasurementUnitModification()).toMeasurementUnitOutput()
    }
}
