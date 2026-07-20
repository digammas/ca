package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.ControllerTestBase
import co.digamma.ca.TestApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

private const val LOCALE = "en"
private const val NAME = "Roast Chicken"
private const val COURSE_NAME = "Main Dish"
private const val CUISINE_NAME = "French"
private const val SERVING_NAME = "Hot"
private const val IMAGE_URL = "https://www.example.com/los_pollos_hermanos.png"
private const val IMAGE_DESCRIPTION = "Los Pollos Hermanos"

private const val CREATE_COURSE_DOCUMENT = """
    mutation {
        createCourse(creation: { locale: "$LOCALE", name: "$COURSE_NAME" }) {
            id
        }
    }
"""

private const val CREATE_CUISINE_DOCUMENT = """
    mutation {
        createCuisine(creation: { locale: "$LOCALE", name: "$CUISINE_NAME" }) {
            id
        }
    }
"""

private const val CREATE_SERVING_DOCUMENT = """
    mutation {
        createServing(creation: { locale: "$LOCALE", name: "$SERVING_NAME" }) {
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

private fun createDishDocument(courseId: String, cuisineId: String, servingId: String) = """
    mutation {
        createDish(creation: {
            locale: "$LOCALE",
            name: "$NAME",
            courseId: "$courseId",
            cuisineId: "$cuisineId",
            servingId: "$servingId",
        }) {
            id
        }
    }
"""

private fun createDishWithImagesDocument(courseId: String, cuisineId: String, servingId: String, imageId: String) = """
    mutation {
        createDish(creation: {
            locale: "$LOCALE",
            name: "$NAME",
            courseId: "$courseId",
            cuisineId: "$cuisineId",
            servingId: "$servingId",
            images: ["$imageId"],
        }) {
            id
        }
    }
"""

private fun createDishWithSideDishesDocument(
    courseId: String,
    cuisineId: String,
    servingId: String,
    sideDishId: String,
) = """
    mutation {
        createDish(creation: {
            locale: "$LOCALE",
            name: "$NAME",
            courseId: "$courseId",
            cuisineId: "$cuisineId",
            servingId: "$servingId",
            sideDishes: ["$sideDishId"],
        }) {
            id
        }
    }
"""

private fun createDishWithSideDishesDocument(
    courseId: String,
    cuisineId: String,
    servingId: String,
    sideDishIds: List<String>,
) = """
    mutation {
        createDish(creation: {
            locale: "$LOCALE",
            name: "$NAME",
            courseId: "$courseId",
            cuisineId: "$cuisineId",
            servingId: "$servingId",
            sideDishes: [${sideDishIds.joinToString(", ") { "\"$it\"" }}],
        }) {
            id
        }
    }
"""

private const val UPDATED_NAME = "Fried Chicken"

private fun updateDishNameDocument(id: String) = """
    mutation {
        updateDish(modification: {
            id: "$id",
            name: "$UPDATED_NAME",
        }) {
            id
            name
        }
    }
"""

private fun updateDishImagesDocument(id: String, imageId: String) = """
    mutation {
        updateDish(modification: {
            id: "$id",
            images: ["$imageId"],
        }) {
            id
        }
    }
"""

private fun updateDishSideDishesDocument(id: String, sideDishIds: List<String>) = """
    mutation {
        updateDish(modification: {
            id: "$id",
            sideDishes: [${sideDishIds.joinToString(", ") { "\"$it\"" }}],
        }) {
            id
        }
    }
"""

private const val GET_DISH_DOCUMENT = $$"""
    query GetDish($id: ID!) {
        dish(id: $id) {
            id
            locale
            name
            course {
                id
                name
            }
            cuisine {
                id
                name
            }
            serving {
                id
                name
            }
            images {
                id
                url
                description
            }
            sideDishes {
                id
            }
        }
    }
"""

private const val DELETE_DISH_DOCUMENT = $$"""
    mutation RemoveDish($id: ID!) {
 	    deleteDish(id: $id)
    }
"""

@SpringBootTest(classes = [TestApplication::class])
@AutoConfigureMockMvc
class DishControllerTest : ControllerTestBase {

    @Autowired
    override lateinit var mockMvc: MockMvc

    private fun createCourseId(): String =
        tester.document(CREATE_COURSE_DOCUMENT)
            .execute()
            .path("data.createCourse.id")
            .hasValue()
            .entity(String::class.java)
            .get()

    private fun createCuisineId(): String =
        tester.document(CREATE_CUISINE_DOCUMENT)
            .execute()
            .path("data.createCuisine.id")
            .hasValue()
            .entity(String::class.java)
            .get()

    private fun createServingId(): String =
        tester.document(CREATE_SERVING_DOCUMENT)
            .execute()
            .path("data.createServing.id")
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

    private fun createDishId(courseId: String, cuisineId: String, servingId: String): String =
        tester.document(createDishDocument(courseId, cuisineId, servingId))
            .execute()
            .path("data.createDish.id")
            .hasValue()
            .entity(String::class.java)
            .get()

    @Test
    fun dish() {
        val courseId = createCourseId()
        val cuisineId = createCuisineId()
        val servingId = createServingId()
        val createdId = createDishId(courseId, cuisineId, servingId)

        tester.document(GET_DISH_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.dish") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("locale").entity(String::class.java).isEqualTo(LOCALE)
                    .path("name").entity(String::class.java).isEqualTo(NAME)
                    .path("course.id").entity(String::class.java).isEqualTo(courseId)
                    .path("course.name").entity(String::class.java).isEqualTo(COURSE_NAME)
                    .path("cuisine.id").entity(String::class.java).isEqualTo(cuisineId)
                    .path("cuisine.name").entity(String::class.java).isEqualTo(CUISINE_NAME)
                    .path("serving.id").entity(String::class.java).isEqualTo(servingId)
                    .path("serving.name").entity(String::class.java).isEqualTo(SERVING_NAME)
                    .path("images").entityList(Any::class.java).hasSize(0)
                    .path("sideDishes").entityList(Any::class.java).hasSize(0)
            }
    }

    @Test
    fun createDish() {
        val courseId = createCourseId()
        val cuisineId = createCuisineId()
        val servingId = createServingId()
        tester.document(createDishDocument(courseId, cuisineId, servingId))
            .execute()
            .path("data.createDish.id")
            .hasValue()
    }

    @Test
    fun createDishWithUnknownCourseFails() {
        val cuisineId = createCuisineId()
        val servingId = createServingId()
        tester.document(createDishDocument("unknown-course-id", cuisineId, servingId))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "NOT_FOUND" }
            .verify()
    }

    @Test
    fun createDishWithImages() {
        val courseId = createCourseId()
        val cuisineId = createCuisineId()
        val servingId = createServingId()
        val imageId = createImageId()

        val createdId = tester.document(createDishWithImagesDocument(courseId, cuisineId, servingId, imageId))
            .execute()
            .path("data.createDish.id")
            .hasValue()
            .entity(String::class.java)
            .get()

        tester.document(GET_DISH_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.dish.images[0].id")
            .entity(String::class.java)
            .isEqualTo(imageId)
            .path("data.dish.images[0].url")
            .entity(String::class.java)
            .isEqualTo(IMAGE_URL)
            .path("data.dish.images[0].description")
            .entity(String::class.java)
            .isEqualTo(IMAGE_DESCRIPTION)
    }

    @Test
    fun createDishWithSideDishes() {
        val courseId = createCourseId()
        val cuisineId = createCuisineId()
        val servingId = createServingId()
        val sideDishId = createDishId(courseId, cuisineId, servingId)

        val createdId = tester.document(createDishWithSideDishesDocument(courseId, cuisineId, servingId, sideDishId))
            .execute()
            .path("data.createDish.id")
            .hasValue()
            .entity(String::class.java)
            .get()

        tester.document(GET_DISH_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.dish.sideDishes")
            .entityList(Any::class.java)
            .hasSize(1)
            .path("data.dish.sideDishes[0].id")
            .entity(String::class.java)
            .isEqualTo(sideDishId)
    }

    @Test
    fun createDishWithSideDishesFailsWhenCandidateAlreadyHasSideDishes() {
        val courseId = createCourseId()
        val cuisineId = createCuisineId()
        val servingId = createServingId()
        val leaf = createDishId(courseId, cuisineId, servingId)
        val mainWithSideDish = tester
            .document(createDishWithSideDishesDocument(courseId, cuisineId, servingId, leaf))
            .execute()
            .path("data.createDish.id")
            .hasValue()
            .entity(String::class.java)
            .get()

        tester.document(createDishWithSideDishesDocument(courseId, cuisineId, servingId, mainWithSideDish))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "BAD_REQUEST" }
            .verify()
    }

    @Test
    fun updateDishName() {
        val courseId = createCourseId()
        val cuisineId = createCuisineId()
        val servingId = createServingId()
        val createdId = createDishId(courseId, cuisineId, servingId)

        tester.document(updateDishNameDocument(createdId))
            .execute()
            .path("data.updateDish") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("name").entity(String::class.java).isEqualTo(UPDATED_NAME)
            }

        tester.document(GET_DISH_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.dish") {
                it
                    .path("name").entity(String::class.java).isEqualTo(UPDATED_NAME)
                    .path("course.id").entity(String::class.java).isEqualTo(courseId)
                    .path("cuisine.id").entity(String::class.java).isEqualTo(cuisineId)
                    .path("serving.id").entity(String::class.java).isEqualTo(servingId)
            }
    }

    @Test
    fun updateDishReplacesImages() {
        val courseId = createCourseId()
        val cuisineId = createCuisineId()
        val servingId = createServingId()
        val oldImageId = createImageId()
        val newImageId = createImageId()

        val createdId = tester.document(createDishWithImagesDocument(courseId, cuisineId, servingId, oldImageId))
            .execute()
            .path("data.createDish.id")
            .hasValue()
            .entity(String::class.java)
            .get()

        tester.document(updateDishImagesDocument(createdId, newImageId))
            .execute()
            .path("data.updateDish.id")
            .hasValue()

        tester.document(GET_DISH_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.dish.images[*].id")
            .entityList(String::class.java)
            .hasSize(1)
            .contains(newImageId)
    }

    @Test
    fun updateDishReplacesSideDishes() {
        val courseId = createCourseId()
        val cuisineId = createCuisineId()
        val servingId = createServingId()
        val kept = createDishId(courseId, cuisineId, servingId)
        val removed = createDishId(courseId, cuisineId, servingId)
        val added = createDishId(courseId, cuisineId, servingId)

        val createdId = tester
            .document(createDishWithSideDishesDocument(courseId, cuisineId, servingId, listOf(kept, removed)))
            .execute()
            .path("data.createDish.id")
            .hasValue()
            .entity(String::class.java)
            .get()

        tester.document(updateDishSideDishesDocument(createdId, listOf(kept, added)))
            .execute()
            .path("data.updateDish.id")
            .hasValue()

        tester.document(GET_DISH_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.dish.sideDishes[*].id")
            .entityList(String::class.java)
            .hasSize(2)
            .contains(kept, added)
    }

    @Test
    fun updateDishFailsWhenDishIsAlreadyUsedAsSideDish() {
        val courseId = createCourseId()
        val cuisineId = createCuisineId()
        val servingId = createServingId()
        val sideDishId = createDishId(courseId, cuisineId, servingId)
        tester.document(createDishWithSideDishesDocument(courseId, cuisineId, servingId, sideDishId))
            .execute()
            .path("data.createDish.id")
            .hasValue()
        val other = createDishId(courseId, cuisineId, servingId)

        tester.document(updateDishSideDishesDocument(sideDishId, listOf(other)))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "BAD_REQUEST" }
            .verify()
    }

    @Test
    fun updateDishFailsWhenListingItselfAsSideDish() {
        val courseId = createCourseId()
        val cuisineId = createCuisineId()
        val servingId = createServingId()
        val createdId = createDishId(courseId, cuisineId, servingId)

        tester.document(updateDishSideDishesDocument(createdId, listOf(createdId)))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "BAD_REQUEST" }
            .verify()
    }

    @Test
    fun deleteDish() {
        val courseId = createCourseId()
        val cuisineId = createCuisineId()
        val servingId = createServingId()
        val createdId = createDishId(courseId, cuisineId, servingId)

        tester.document(DELETE_DISH_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.deleteDish")
            .entity(Boolean::class.java)
            .isEqualTo(true)
        tester.document(GET_DISH_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "NOT_FOUND" }
            .verify()
    }
}