package co.digamma.ca.suites.cookbook

import co.digamma.ca.domain.api.cookbook.MeasurementUnit
import co.digamma.ca.domain.spi.cookbook.MeasurementUnitRepository
import co.digamma.ca.fixtures.utils.RandGen
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase
import java.util.Locale

abstract class MeasurementUnitRepositoryTestBase : CrudRepositoryTestBase<MeasurementUnit>() {

    abstract override val sut: MeasurementUnitRepository

    override fun newModel() = MeasurementUnit(
        id = RandGen.uuid(),
        locale = Locale.ENGLISH,
        name = RandGen.string(),
        dimension = MeasurementUnit.Dimension.QUANTITY,
    )

    override fun modifyModel(model: MeasurementUnit) = MeasurementUnit(
        id = model.id,
        locale = Locale.FRENCH,
        name = RandGen.string(),
        dimension = MeasurementUnit.Dimension.WEIGHT,
    )
}

