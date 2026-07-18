package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.domain.api.cookbook.MeasurementUnit
import co.digamma.ca.domain.api.cookbook.MeasurementUnitModification


data class MeasurementUnitModificationInput(
    private val id: String,
    private val name: String?,
    private val dimension: MeasurementUnit.Dimension?,
) {

    fun toMeasurementUnitModification() = MeasurementUnitModification(
        id = this.id,
        name = this.name,
        dimension = this.dimension,
    )
}
