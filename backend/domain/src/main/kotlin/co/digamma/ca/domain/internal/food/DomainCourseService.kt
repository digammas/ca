package co.digamma.ca.domain.internal.food

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.food.Course
import co.digamma.ca.domain.api.food.CourseCreation
import co.digamma.ca.domain.api.food.CourseModification
import co.digamma.ca.domain.api.food.CourseService
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.spi.food.CourseRepository

@Singleton
open class DomainCourseService(
    override val repository: CourseRepository,
) : DefaultCurdService<Course, CourseCreation, CourseModification>(), CourseService {

    override fun toModel(creation: CourseCreation) = Course(
        id = generateId(),
        locale = creation.locale,
        name = creation.name,
    )

    override fun toModel(modification: CourseModification, existing: Course) = Course(
            id = existing.id,
            locale = existing.locale,
            name = modification.name ?: existing.name,
        )
}
