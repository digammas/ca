package co.digamma.ca.controllers.graphql.media

import co.digamma.ca.TestApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.HttpGraphQlTester
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.MockMvcWebTestClient

private const val LOCALE = "en"
private const val URL = "https://upload.wikimedia.org/wikipedia/en/7/7d/Lenna_%28test_image%29.png"
private const val DESCRIPTION = "Lenna"

private const val GET_IMAGE_DOCUMENT = """
    query GetImage(${"$"}id: ID!) {
 	    image(id: ${"$"}id) {
    	    id
    	    description
    	    url
	    }
    }
"""

private const val CREATE_IMAGE_DOCUMENT = """
    mutation {
        createImage(creation: {
            locale: "$LOCALE",
  	        url: "$URL",
  	        description: "$DESCRIPTION"
        }) {
            id
        }
}
"""

private const val DELETE_IMAGE_DOCUMENT = """
    mutation RemoveImage(${"$"}id: ID!) {
 	    deleteImage(id: ${"$"}id)
    }
"""

@SpringBootTest(classes = [TestApplication::class])
@AutoConfigureMockMvc
class ImageControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val client
        get(): WebTestClient =
            MockMvcWebTestClient.bindTo(mockMvc)
                .baseUrl("/graphql")
                .build()

    private val tester
        get(): HttpGraphQlTester =
            HttpGraphQlTester.create(client)

    @Test
    fun image() {
        val createdId = tester.document(CREATE_IMAGE_DOCUMENT)
            .execute()
            .path("data.createImage.id")
            .hasValue()
            .entity(String::class.java)
            .get()
        tester.document(GET_IMAGE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.image") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("description").entity(String::class.java).isEqualTo(DESCRIPTION)
                    .path("url").entity(String::class.java).isEqualTo(URL)
            }
    }

    @Test
    fun createImage() {
        tester.document(CREATE_IMAGE_DOCUMENT)
            .execute()
            .path("data.createImage.id")
            .hasValue()
    }

    @Test
    fun deleteImage() {
        val createdId = tester.document(CREATE_IMAGE_DOCUMENT)
            .execute()
            .path("data.createImage.id")
            .hasValue()
            .entity(String::class.java)
            .get()
        tester.document(DELETE_IMAGE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.deleteImage")
            .entity(Boolean::class.java)
            .isEqualTo(true)
        tester.document(GET_IMAGE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .errors()
            .expect { it.errorType.toString() === "NOT_FOUND" }
            .verify()
    }
}