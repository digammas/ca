package co.digamma.ca.persistence.inmem.food

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.food.Course
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.persistence.inmem.InMemCrudRepository

@Singleton
class InMemCourseRepository: InMemCrudRepository<Course>(), CourseRepository