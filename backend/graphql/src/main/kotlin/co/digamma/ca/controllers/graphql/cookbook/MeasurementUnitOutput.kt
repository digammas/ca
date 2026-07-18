package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.domain.api.cookbook.MeasurementUnit

data class MeasurementUnitOutput(
    val id: String,
    val locale: String,
    val name: String,
    val dimension: MeasurementUnit.Dimension,
)

fun MeasurementUnit.toMeasurementUnitOutput() = MeasurementUnitOutput(
    id = this.id,
    locale = this.locale.toString(),
    name = this.name,
    dimension = this.dimension,
)
