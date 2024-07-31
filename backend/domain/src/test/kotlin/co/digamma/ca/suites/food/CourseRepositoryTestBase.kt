package co.digamma.ca.suites.food

import co.digamma.ca.domain.api.food.Course
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.fixtures.utils.RandGen
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase
import java.util.Locale

abstract class CourseRepositoryTestBase : CrudRepositoryTestBase<Course>() {

    abstract override val sut: CourseRepository

    override fun newModel() = Course(
        id = RandGen.uuid(),
        locale = Locale.ENGLISH,
        name = RandGen.string()
    )

    override fun modifyModel(model: Course) = model.copy(
        locale = Locale.FRENCH,
        name = RandGen.string()
    )
}
