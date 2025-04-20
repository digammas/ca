package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.controllers.graphql.PageQuery
import co.digamma.ca.controllers.graphql.asConnection
import co.digamma.ca.domain.api.food.CourseService
import graphql.relay.Connection
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class CourseController(
    private val service: CourseService,
) {
    @QueryMapping
    fun course(@Argument id: String): CourseOutput {
        return service.retrieve(id).toCourseOutput()
    }

    @QueryMapping
    fun courses(@Argument query: PageQuery): Connection<CourseOutput> {
        return service.retrieve(query.asSpecs())
            .asConnection { it.toCourseOutput() }
    }

    @MutationMapping
    fun deleteCourse(@Argument id: String): Boolean {
        service.delete(id)
        return true
    }

    @MutationMapping
    fun createCourse(@Argument creation: CourseCreationInput): CourseOutput {
        return service.create(creation.toCourseCreation()).toCourseOutput()
    }
}
