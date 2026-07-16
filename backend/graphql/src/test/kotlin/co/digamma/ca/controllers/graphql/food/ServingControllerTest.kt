package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.ControllerTestBase
import co.digamma.ca.TestApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

private const val LOCALE = "en"
private const val NAME = "Cold"
private const val UPDATED_NAME = "Hot"

private const val GET_SERVING_DOCUMENT = $$"""
    query GetServing($id: ID!) {
 	    serving(id: $id) {
    	    id
    	    locale
    	    name
	    }
    }
"""

private const val CREATE_SERVING_DOCUMENT = """
    mutation {
        createServing(creation: {
            locale: "$LOCALE",
  	        name: "$NAME",
        }) {
            id
        }
}
"""

private const val UPDATE_SERVING_DOCUMENT = $$"""
    mutation UpdateServing($id: ID!) {
        updateServing(modification: {
            id: $id,
            name: "Hot",
        }) {
            id
            locale
            name
        }
    }
"""

private const val DELETE_SERVING_DOCUMENT = $$"""
    mutation RemoveServing($id: ID!) {
 	    deleteServing(id: $id)
    }
"""

@SpringBootTest(classes = [TestApplication::class])
@AutoConfigureMockMvc
class ServingControllerTest : ControllerTestBase {

    @Autowired
    override lateinit var mockMvc: MockMvc

    @Test
    fun serving() {
        val createdId = tester.document(CREATE_SERVING_DOCUMENT)
            .execute()
            .path("data.createServing.id")
            .hasValue()
            .entity(String::class.java)
            .get()
        tester.document(GET_SERVING_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.serving") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("locale").entity(String::class.java).isEqualTo(LOCALE)
                    .path("name").entity(String::class.java).isEqualTo(NAME)
            }
    }

    @Test
    fun createServing() {
        tester.document(CREATE_SERVING_DOCUMENT)
            .execute()
            .path("data.createServing.id")
            .hasValue()
    }

    @Test
    fun updateServing() {
        val createdId = tester.document(CREATE_SERVING_DOCUMENT)
            .execute()
            .path("data.createServing.id")
            .hasValue()
            .entity(String::class.java)
            .get()
        tester.document(UPDATE_SERVING_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.updateServing") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("locale").entity(String::class.java).isEqualTo(LOCALE)
                    .path("name").entity(String::class.java).isEqualTo(UPDATED_NAME)
            }
        tester.document(GET_SERVING_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.serving.name")
            .entity(String::class.java)
            .isEqualTo(UPDATED_NAME)
    }

    @Test
    fun deleteServing() {
        val createdId = tester.document(CREATE_SERVING_DOCUMENT)
            .execute()
            .path("data.createServing.id")
            .hasValue()
            .entity(String::class.java)
            .get()
        tester.document(DELETE_SERVING_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.deleteServing")
            .entity(Boolean::class.java)
            .isEqualTo(true)
        tester.document(GET_SERVING_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "NOT_FOUND" }
            .verify()
    }
}