package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.domain.api.cookbook.MeasurementUnit
import co.digamma.ca.domain.api.cookbook.MeasurementUnitCreation
import java.util.Locale


data class MeasurementUnitCreationInput(
    private val locale: String,
    private val name: String,
    private val dimension: MeasurementUnit.Dimension,
) {

    fun toMeasurementUnitCreation() = MeasurementUnitCreation(
        locale = Locale.forLanguageTag(this.locale),
        name = this.name,
        dimension = this.dimension,
    )
}
