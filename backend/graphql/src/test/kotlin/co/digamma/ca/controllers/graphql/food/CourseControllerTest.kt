package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.ControllerTestBase
import co.digamma.ca.TestApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

private const val LOCALE = "en"
private const val NAME = "Main Dish"

private const val GET_COURSE_DOCUMENT = $$"""
    query GetCourse($id: ID!) {
 	    course(id: $id) {
    	    id
    	    locale
    	    name
	    }
    }
"""

private const val CREATE_COURSE_DOCUMENT = """
    mutation {
        createCourse(creation: {
            locale: "$LOCALE",
  	        name: "$NAME",
        }) {
            id
        }
}
"""

private const val DELETE_COURSE_DOCUMENT = $$"""
    mutation RemoveCourse($id: ID!) {
 	    deleteCourse(id: $id)
    }
"""

@SpringBootTest(classes = [TestApplication::class])
@AutoConfigureMockMvc
class CourseControllerTest : ControllerTestBase {

    @Autowired
    override lateinit var mockMvc: MockMvc

    @Test
    fun course() {
        val createdId = tester.document(CREATE_COURSE_DOCUMENT)
            .execute()
            .path("data.createCourse.id")
            .hasValue()
            .entity(String::class.java)
            .get()
        tester.document(GET_COURSE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.course") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("locale").entity(String::class.java).isEqualTo(LOCALE)
                    .path("name").entity(String::class.java).isEqualTo(NAME)
            }
    }

    @Test
    fun createCourse() {
        tester.document(CREATE_COURSE_DOCUMENT)
            .execute()
            .path("data.createCourse.id")
            .hasValue()
    }

    @Test
    fun deleteCourse() {
        val createdId = tester.document(CREATE_COURSE_DOCUMENT)
            .execute()
            .path("data.createCourse.id")
            .hasValue()
            .entity(String::class.java)
            .get()
        tester.document(DELETE_COURSE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.deleteCourse")
            .entity(Boolean::class.java)
            .isEqualTo(true)
        tester.document(GET_COURSE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "NOT_FOUND" }
            .verify()
    }
}