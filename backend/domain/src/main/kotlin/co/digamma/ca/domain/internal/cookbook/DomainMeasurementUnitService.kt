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
) : DefaultCurdService<MeasurementUnit>(), MeasurementUnitService {

    override fun create(creation: MeasurementUnitCreation): MeasurementUnit {
        val model = MeasurementUnit(
            id = generateId(),
            locale = creation.locale,
            name = creation.name,
            dimension = creation.dimension,
        )
        return repository.create(model)
    }

    override fun update(modification: MeasurementUnitModification): MeasurementUnit {
        val existing = this.retrieve(modification.id)
        val model = MeasurementUnit(
            id = existing.id,
            locale = existing.locale,
            name = modification.name ?: existing.name,
            dimension = modification.dimension ?: existing.dimension,
        )
        return repository.update(model)
    }
}

