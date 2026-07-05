package co.digamma.ca.domain.api.cookbook

import co.digamma.ca.domain.api.CrudService
import co.digamma.ca.domain.api.model.ModelReference
import java.util.Locale

interface MeasurementUnitService : CrudService<MeasurementUnit, MeasurementUnitCreation, MeasurementUnitModification>

interface MeasurementUnitMutation {
    val name: String?
    val dimension: MeasurementUnit.Dimension?
}

data class MeasurementUnitCreation(
    val locale: Locale,
    override val name: String,
    override val dimension: MeasurementUnit.Dimension,
) : MeasurementUnitMutation

data class MeasurementUnitModification(
    override val id: String,
    override val name: String?,
    override val dimension: MeasurementUnit.Dimension?,
) : MeasurementUnitMutation, ModelReference<MeasurementUnit>
