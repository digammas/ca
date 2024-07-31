package co.digamma.ca.fixtures.inmem.food

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.food.Course
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.fixtures.inmem.InMemCrudRepository

@Singleton
class InMemCourseRepository : InMemCrudRepository<Course>(), CourseRepository