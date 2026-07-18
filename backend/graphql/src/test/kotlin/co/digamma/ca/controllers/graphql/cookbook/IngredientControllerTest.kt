package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.ControllerTestBase
import co.digamma.ca.TestApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

private const val LOCALE = "en"
private const val NAME = "Tomato"
private const val DESCRIPTION = "A juicy red fruit used as a vegetable"
private const val UPDATED_NAME = "Cherry Tomato"
private const val UPDATED_DESCRIPTION = "A small juicy red fruit used as a vegetable"
private const val IMAGE_URL = "https://www.example.com/tomato.png"
private const val IMAGE_DESCRIPTION = "A ripe tomato"

private const val CREATE_INGREDIENT_DOCUMENT = """
    mutation {
        createIngredient(creation: {
            locale: "$LOCALE",
            name: "$NAME",
            description: "$DESCRIPTION",
        }) {
            id
        }
    }
"""

private const val CREATE_IMAGE_DOCUMENT = """
    mutation {
        createImage(creation: {
            locale: "$LOCALE",
            url: "$IMAGE_URL",
            description: "$IMAGE_DESCRIPTION"
        }) {
            id
        }
    }
"""

private fun createIngredientWithImagesDocument(imageId: String) = """
    mutation {
        createIngredient(creation: {
            locale: "$LOCALE",
            name: "$NAME",
            description: "$DESCRIPTION",
            images: ["$imageId"],
        }) {
            id
        }
    }
"""

private const val GET_INGREDIENT_DOCUMENT = $$"""
    query GetIngredient($id: ID!) {
        ingredient(id: $id) {
            id
            locale
            name
            description
            images {
                id
                url
                description
            }
        }
    }
"""

private const val UPDATE_INGREDIENT_DOCUMENT = $$"""
    mutation UpdateIngredient($id: ID!) {
        updateIngredient(modification: {
            id: $id,
            name: "Cherry Tomato",
            description: "A small juicy red fruit used as a vegetable",
        }) {
            id
            locale
            name
            description
        }
    }
"""

private const val DELETE_INGREDIENT_DOCUMENT = $$"""
    mutation RemoveIngredient($id: ID!) {
 	    deleteIngredient(id: $id)
    }
"""

@SpringBootTest(classes = [TestApplication::class])
@AutoConfigureMockMvc
class IngredientControllerTest : ControllerTestBase {

    @Autowired
    override lateinit var mockMvc: MockMvc

    private fun createIngredientId(): String =
        tester.document(CREATE_INGREDIENT_DOCUMENT)
            .execute()
            .path("data.createIngredient.id")
            .hasValue()
            .entity(String::class.java)
            .get()

    private fun createImageId(): String =
        tester.document(CREATE_IMAGE_DOCUMENT)
            .execute()
            .path("data.createImage.id")
            .hasValue()
            .entity(String::class.java)
            .get()

    @Test
    fun ingredient() {
        val createdId = createIngredientId()
        tester.document(GET_INGREDIENT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.ingredient") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("locale").entity(String::class.java).isEqualTo(LOCALE)
                    .path("name").entity(String::class.java).isEqualTo(NAME)
                    .path("description").entity(String::class.java).isEqualTo(DESCRIPTION)
                    .path("images").entityList(Any::class.java).hasSize(0)
            }
    }

    @Test
    fun createIngredient() {
        tester.document(CREATE_INGREDIENT_DOCUMENT)
            .execute()
            .path("data.createIngredient.id")
            .hasValue()
    }

    @Test
    fun createIngredientWithImages() {
        val imageId = createImageId()
        val createdId = tester.document(createIngredientWithImagesDocument(imageId))
            .execute()
            .path("data.createIngredient.id")
            .hasValue()
            .entity(String::class.java)
            .get()

        tester.document(GET_INGREDIENT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.ingredient.images[0].id")
            .entity(String::class.java)
            .isEqualTo(imageId)
            .path("data.ingredient.images[0].url")
            .entity(String::class.java)
            .isEqualTo(IMAGE_URL)
            .path("data.ingredient.images[0].description")
            .entity(String::class.java)
            .isEqualTo(IMAGE_DESCRIPTION)
    }

    @Test
    fun createIngredientWithUnknownImageFails() {
        tester.document(createIngredientWithImagesDocument("unknown-image-id"))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "NOT_FOUND" }
            .verify()
    }

    @Test
    fun updateIngredient() {
        val createdId = createIngredientId()
        tester.document(UPDATE_INGREDIENT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.updateIngredient") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("locale").entity(String::class.java).isEqualTo(LOCALE)
                    .path("name").entity(String::class.java).isEqualTo(UPDATED_NAME)
                    .path("description").entity(String::class.java).isEqualTo(UPDATED_DESCRIPTION)
            }
        tester.document(GET_INGREDIENT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.ingredient.name")
            .entity(String::class.java)
            .isEqualTo(UPDATED_NAME)
    }

    @Test
    fun deleteIngredient() {
        val createdId = createIngredientId()
        tester.document(DELETE_INGREDIENT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.deleteIngredient")
            .entity(Boolean::class.java)
            .isEqualTo(true)
        tester.document(GET_INGREDIENT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "NOT_FOUND" }
            .verify()
    }
}
