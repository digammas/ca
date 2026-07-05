package co.digamma.ca.domain.internal.cookbook

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.cookbook.MeasurementUnit
import co.digamma.ca.domain.api.cookbook.MeasurementUnitCreation
import co.digamma.ca.domain.api.cookbook.MeasurementUnitModification
import co.digamma.ca.domain.api.cookbook.MeasurementUnitService
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.spi.cookbook.MeasurementUnitRepository

@Singleton
open class DomainMeasurementUnitService(
    override val repository: MeasurementUnitRepository,
) : DefaultCurdService<MeasurementUnit, MeasurementUnitCreation, MeasurementUnitModification>(), MeasurementUnitService {

    override fun toModel(creation: MeasurementUnitCreation) = MeasurementUnit(
        id = generateId(),
        locale = creation.locale,
        name = creation.name,
        dimension = creation.dimension,
    )

    override fun toModel(modification: MeasurementUnitModification, existing: MeasurementUnit) = MeasurementUnit(
            id = existing.id,
            locale = existing.locale,
            name = modification.name ?: existing.name,
            dimension = modification.dimension ?: existing.dimension,
        )
}

