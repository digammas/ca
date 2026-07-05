package co.digamma.ca.tests.inmem.cookbook

import co.digamma.ca.fixtures.inmem.cookbook.InMemMeasurementUnitRepository
import co.digamma.ca.suites.cookbook.MeasurementUnitRepositoryTestBase

class InMemMeasurementUnitRepositoryTest : MeasurementUnitRepositoryTestBase() {
    override val sut = InMemMeasurementUnitRepository()
}

