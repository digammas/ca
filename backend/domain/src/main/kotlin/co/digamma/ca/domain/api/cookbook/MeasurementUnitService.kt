package co.digamma.ca.domain.api.cookbook

import co.digamma.ca.domain.api.DeleteService
import co.digamma.ca.domain.api.RetrieveService

interface MeasurementUnitService : RetrieveService<MeasurementUnit>, DeleteService {
    fun create(measurementUnit: MeasurementUnitCreation): MeasurementUnit
    fun update(measurementUnit: MeasurementUnitModification): MeasurementUnit
}

interface MeasurementUnitMutation {
    val name: String?
    val dimension: MeasurementUnit.Dimension?
}

data class MeasurementUnitCreation(
    val locale: String,
    override val name: String,
    override val dimension: MeasurementUnit.Dimension,
) : MeasurementUnitMutation

data class MeasurementUnitModification(
    val id: String,
    override val name: String?,
    override val dimension: MeasurementUnit.Dimension?,
) : MeasurementUnitMutation
