package co.digamma.ca.domain.internal.food

import co.digamma.ca.domain.api.food.Course
import co.digamma.ca.domain.api.food.CourseCreation
import co.digamma.ca.domain.api.food.CourseModification
import co.digamma.ca.domain.api.food.CourseService
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.spi.food.CourseRepository

class DomainCourseService(
    override val repository: CourseRepository,
) : DefaultCurdService<Course>(), CourseService {

    override fun create(creation: CourseCreation): Course {
        val course = Course(
            id = generateId(),
            locale = creation.locale,
            name = creation.name,
        )
        return this.repository.create(course)
    }

    override fun update(modification: CourseModification): Course {
        val existing = this.retrieve(modification.id)
        val course = Course(
            id = existing.id,
            locale = existing.locale,
            name = modification.name ?: existing.name,
        )
        return this.repository.update(course)
    }

}
