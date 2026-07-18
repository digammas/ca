package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.ControllerTestBase
import co.digamma.ca.TestApplication
import co.digamma.ca.domain.api.users.User
import co.digamma.ca.domain.spi.users.UserRepository
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

private const val LOCALE = "en"
private const val DISH_NAME = "Roast Chicken"
private const val COURSE_NAME = "Main Dish"
private const val CUISINE_NAME = "French"
private const val SERVING_NAME = "Hot"
private const val INGREDIENT_NAME = "Chicken"
private const val INGREDIENT_DESCRIPTION = "A whole chicken"
private const val MEASUREMENT_UNIT_NAME = "Kilogram"
private const val MEASUREMENT_UNIT_DIMENSION = "WEIGHT"
private const val YIELD = 4
private const val TIME_TO_SERVE = 90
private const val STEP_DESCRIPTION = "Roast the chicken in the oven"
private const val STEP_ESTIMATED_TIME = 75
private const val QUANTITY = "1.5"
private const val UPDATED_YIELD = 6
private const val UPDATED_TIME_TO_SERVE = 120
private const val IMAGE_URL = "https://www.example.com/roast_chicken.png"
private const val IMAGE_DESCRIPTION = "A golden roast chicken"

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

private fun createDishDocument(courseId: String, cuisineId: String, servingId: String) = """
    mutation {
        createDish(creation: {
            locale: "$LOCALE",
            name: "$DISH_NAME",
            courseId: "$courseId",
            cuisineId: "$cuisineId",
            servingId: "$servingId",
        }) {
            id
        }
    }
"""

private const val CREATE_INGREDIENT_DOCUMENT = """
    mutation {
        createIngredient(creation: {
            locale: "$LOCALE",
            name: "$INGREDIENT_NAME",
            description: "$INGREDIENT_DESCRIPTION",
        }) {
            id
        }
    }
"""

private const val CREATE_MEASUREMENT_UNIT_DOCUMENT = """
    mutation {
        createMeasurementUnit(creation: {
            locale: "$LOCALE",
            name: "$MEASUREMENT_UNIT_NAME",
            dimension: $MEASUREMENT_UNIT_DIMENSION,
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

private fun createRecipeWithImagesDocument(dishId: String, author: String, imageId: String) = """
    mutation {
        createRecipe(creation: {
            locale: "$LOCALE",
            dishId: "$dishId",
            yield: $YIELD,
            timeToServe: $TIME_TO_SERVE,
            author: "$author",
            images: ["$imageId"],
        }) {
            id
        }
    }
"""

private fun createRecipeDocument(dishId: String, author: String) = """
    mutation {
        createRecipe(creation: {
            locale: "$LOCALE",
            dishId: "$dishId",
            yield: $YIELD,
            timeToServe: $TIME_TO_SERVE,
            author: "$author",
        }) {
            id
        }
    }
"""

private fun createRecipeWithStepsAndIngredientsDocument(
    dishId: String,
    author: String,
    ingredientId: String,
    measurementUnitId: String,
) = """
    mutation {
        createRecipe(creation: {
            locale: "$LOCALE",
            dishId: "$dishId",
            yield: $YIELD,
            timeToServe: $TIME_TO_SERVE,
            author: "$author",
            steps: [{
                description: "$STEP_DESCRIPTION",
                estimatedTime: $STEP_ESTIMATED_TIME,
            }],
            ingredients: [{
                ingredientId: "$ingredientId",
                measurementUnitId: "$measurementUnitId",
                quantity: $QUANTITY,
            }],
        }) {
            id
        }
    }
"""

private fun createRecipeWithoutTimeToServeDocument(dishId: String, author: String) = """
    mutation {
        createRecipe(creation: {
            locale: "$LOCALE",
            dishId: "$dishId",
            yield: $YIELD,
            author: "$author",
        }) {
            id
            timeToServe
        }
    }
"""

private const val GET_RECIPE_DOCUMENT = $$"""
    query GetRecipe($id: ID!) {
        recipe(id: $id) {
            id
            locale
            yield
            createdAt
            updatedAt
            author
            timeToServe
            dish {
                id
                name
            }
            images {
                id
            }
            steps {
                id
                locale
                description
                estimatedTime
            }
            ingredients {
                id
                quantity
                ingredient {
                    id
                    name
                }
                unit {
                    id
                    name
                }
            }
        }
    }
"""

private const val UPDATE_RECIPE_DOCUMENT = $$"""
    mutation UpdateRecipe($id: ID!) {
        updateRecipe(modification: {
            id: $id,
            yield: 6,
            timeToServe: 120,
        }) {
            id
            yield
            timeToServe
        }
    }
"""

private const val DELETE_RECIPE_DOCUMENT = $$"""
    mutation RemoveRecipe($id: ID!) {
 	    deleteRecipe(id: $id)
    }
"""

@SpringBootTest(classes = [TestApplication::class])
@AutoConfigureMockMvc
class RecipeControllerTest : ControllerTestBase {

    @Autowired
    override lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userRepository: UserRepository

    private fun createUsername(): String =
        userRepository.create(
            User(
                username = "user-${UUID.randomUUID()}",
                password = "secret",
                email = "user@example.com",
            )
        ).username

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

    private fun createDishId(): String =
        tester.document(createDishDocument(createCourseId(), createCuisineId(), createServingId()))
            .execute()
            .path("data.createDish.id")
            .hasValue()
            .entity(String::class.java)
            .get()

    private fun createIngredientId(): String =
        tester.document(CREATE_INGREDIENT_DOCUMENT)
            .execute()
            .path("data.createIngredient.id")
            .hasValue()
            .entity(String::class.java)
            .get()

    private fun createMeasurementUnitId(): String =
        tester.document(CREATE_MEASUREMENT_UNIT_DOCUMENT)
            .execute()
            .path("data.createMeasurementUnit.id")
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

    private fun createRecipeId(dishId: String, author: String): String =
        tester.document(createRecipeDocument(dishId, author))
            .execute()
            .path("data.createRecipe.id")
            .hasValue()
            .entity(String::class.java)
            .get()

    @Test
    fun recipe() {
        val dishId = createDishId()
        val author = createUsername()
        val ingredientId = createIngredientId()
        val measurementUnitId = createMeasurementUnitId()
        val createdId = tester
            .document(createRecipeWithStepsAndIngredientsDocument(dishId, author, ingredientId, measurementUnitId))
            .execute()
            .path("data.createRecipe.id")
            .hasValue()
            .entity(String::class.java)
            .get()

        tester.document(GET_RECIPE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.recipe") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("locale").entity(String::class.java).isEqualTo(LOCALE)
                    .path("yield").entity(Int::class.java).isEqualTo(YIELD)
                    .path("createdAt").hasValue()
                    .path("updatedAt").hasValue()
                    .path("author").entity(String::class.java).isEqualTo(author)
                    .path("timeToServe").entity(Int::class.java).isEqualTo(TIME_TO_SERVE)
                    .path("dish.id").entity(String::class.java).isEqualTo(dishId)
                    .path("dish.name").entity(String::class.java).isEqualTo(DISH_NAME)
                    .path("images").entityList(Any::class.java).hasSize(0)
                    .path("steps").entityList(Any::class.java).hasSize(1)
                    .path("steps[0].locale").entity(String::class.java).isEqualTo(LOCALE)
                    .path("steps[0].description").entity(String::class.java).isEqualTo(STEP_DESCRIPTION)
                    .path("steps[0].estimatedTime").entity(Int::class.java).isEqualTo(STEP_ESTIMATED_TIME)
                    .path("ingredients").entityList(Any::class.java).hasSize(1)
                    .path("ingredients[0].quantity").entity(Double::class.java).isEqualTo(QUANTITY.toDouble())
                    .path("ingredients[0].ingredient.id").entity(String::class.java).isEqualTo(ingredientId)
                    .path("ingredients[0].ingredient.name").entity(String::class.java).isEqualTo(INGREDIENT_NAME)
                    .path("ingredients[0].unit.id").entity(String::class.java).isEqualTo(measurementUnitId)
                    .path("ingredients[0].unit.name").entity(String::class.java).isEqualTo(MEASUREMENT_UNIT_NAME)
            }
    }

    @Test
    fun createRecipe() {
        tester.document(createRecipeDocument(createDishId(), createUsername()))
            .execute()
            .path("data.createRecipe.id")
            .hasValue()
    }

    @Test
    fun createRecipeWithoutTimeToServeDefaultsToZero() {
        tester.document(createRecipeWithoutTimeToServeDocument(createDishId(), createUsername()))
            .execute()
            .path("data.createRecipe.timeToServe")
            .entity(Int::class.java)
            .isEqualTo(0)
    }

    @Test
    fun createRecipeWithUnknownDishFails() {
        tester.document(createRecipeDocument("unknown-dish-id", createUsername()))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "NOT_FOUND" }
            .verify()
    }

    @Test
    fun createRecipeWithUnknownAuthorFails() {
        tester.document(createRecipeDocument(createDishId(), "unknown-author"))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "NOT_FOUND" }
            .verify()
    }

    @Test
    fun updateRecipe() {
        val createdId = createRecipeId(createDishId(), createUsername())
        tester.document(UPDATE_RECIPE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.updateRecipe") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("yield").entity(Int::class.java).isEqualTo(UPDATED_YIELD)
                    .path("timeToServe").entity(Int::class.java).isEqualTo(UPDATED_TIME_TO_SERVE)
            }
        tester.document(GET_RECIPE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.recipe.yield")
            .entity(Int::class.java)
            .isEqualTo(UPDATED_YIELD)
    }

    @Test
    fun updateRecipeKeepsImages() {
        val imageId = createImageId()
        val createdId = tester
            .document(createRecipeWithImagesDocument(createDishId(), createUsername(), imageId))
            .execute()
            .path("data.createRecipe.id")
            .hasValue()
            .entity(String::class.java)
            .get()

        tester.document(UPDATE_RECIPE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.updateRecipe.yield")
            .entity(Int::class.java)
            .isEqualTo(UPDATED_YIELD)

        tester.document(GET_RECIPE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.recipe.images")
            .entityList(Any::class.java)
            .hasSize(1)
            .path("data.recipe.images[0].id")
            .entity(String::class.java)
            .isEqualTo(imageId)
    }

    @Test
    fun deleteRecipe() {
        val createdId = createRecipeId(createDishId(), createUsername())
        tester.document(DELETE_RECIPE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.deleteRecipe")
            .entity(Boolean::class.java)
            .isEqualTo(true)
        tester.document(GET_RECIPE_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "NOT_FOUND" }
            .verify()
    }
}
