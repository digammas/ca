package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.ControllerTestBase
import co.digamma.ca.TestApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

private const val LOCALE = "en"
private const val NAME = "Italian"
private const val UPDATED_NAME = "French"

private const val GET_CUISINE_DOCUMENT = $$"""
    query GetCuisine($id: ID!) {
 	    cuisine(id: $id) {
    	    id
    	    locale
    	    name
	    }
    }
"""

private const val CREATE_CUISINE_DOCUMENT = """
    mutation {
        createCuisine(creation: {
            locale: "$LOCALE",
  	        name: "$NAME",
        }) {
            id
        }
}
"""

private const val UPDATE_CUISINE_DOCUMENT = $$"""
    mutation UpdateCuisine($id: ID!) {
        updateCuisine(modification: {
            id: $id,
            name: "French",
        }) {
            id
            locale
            name
        }
    }
"""

private const val DELETE_CUISINE_DOCUMENT = $$"""
    mutation RemoveCuisine($id: ID!) {
 	    deleteCuisine(id: $id)
    }
"""

@SpringBootTest(classes = [TestApplication::class])
@AutoConfigureMockMvc
class CuisineControllerTest : ControllerTestBase {

    @Autowired
    override lateinit var mockMvc: MockMvc

    @Test
    fun cuisine() {
        val createdId = tester.document(CREATE_CUISINE_DOCUMENT)
            .execute()
            .path("data.createCuisine.id")
            .hasValue()
            .entity(String::class.java)
            .get()
        tester.document(GET_CUISINE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.cuisine") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("locale").entity(String::class.java).isEqualTo(LOCALE)
                    .path("name").entity(String::class.java).isEqualTo(NAME)
            }
    }

    @Test
    fun createCuisine() {
        tester.document(CREATE_CUISINE_DOCUMENT)
            .execute()
            .path("data.createCuisine.id")
            .hasValue()
    }

    @Test
    fun updateCuisine() {
        val createdId = tester.document(CREATE_CUISINE_DOCUMENT)
            .execute()
            .path("data.createCuisine.id")
            .hasValue()
            .entity(String::class.java)
            .get()
        tester.document(UPDATE_CUISINE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.updateCuisine") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("locale").entity(String::class.java).isEqualTo(LOCALE)
                    .path("name").entity(String::class.java).isEqualTo(UPDATED_NAME)
            }
        tester.document(GET_CUISINE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.cuisine.name")
            .entity(String::class.java)
            .isEqualTo(UPDATED_NAME)
    }

    @Test
    fun deleteCuisine() {
        val createdId = tester.document(CREATE_CUISINE_DOCUMENT)
            .execute()
            .path("data.createCuisine.id")
            .hasValue()
            .entity(String::class.java)
            .get()
        tester.document(DELETE_CUISINE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.deleteCuisine")
            .entity(Boolean::class.java)
            .isEqualTo(true)
        tester.document(GET_CUISINE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "NOT_FOUND" }
            .verify()
    }
}