package co.digamma.ca.suites.food

import co.digamma.ca.domain.api.food.Course
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.fixtures.givenLocale
import co.digamma.ca.fixtures.givenString
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase

abstract class CourseRepositoryTestBase : CrudRepositoryTestBase<Course>() {

    abstract override val sut: CourseRepository

    override fun newModel() = givenCourse()

    override fun modifyModel(model: Course) = model.copy(
        locale = givenLocale(),
        name = givenString()
    )
}
