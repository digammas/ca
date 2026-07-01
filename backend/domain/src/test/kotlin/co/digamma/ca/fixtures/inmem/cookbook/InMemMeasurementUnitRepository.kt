package co.digamma.ca.fixtures.inmem.cookbook

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.cookbook.MeasurementUnit
import co.digamma.ca.domain.spi.cookbook.MeasurementUnitRepository
import co.digamma.ca.fixtures.inmem.InMemCrudRepository

@Singleton
class InMemMeasurementUnitRepository : InMemCrudRepository<MeasurementUnit>(), MeasurementUnitRepository

