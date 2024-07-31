package co.digamma.ca.tests.inmem.food

import co.digamma.ca.fixtures.inmem.food.InMemCourseRepository
import co.digamma.ca.suites.food.CourseRepositoryTestBase

class InMemCourseRepositoryTest : CourseRepositoryTestBase() {
    override val sut = InMemCourseRepository()
}