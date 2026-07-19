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
private const val YIELD = 4
private const val TIME_TO_SERVE = 90
private const val INGREDIENT_NAME = "Chicken"
private const val INGREDIENT_DESCRIPTION = "A whole chicken"
private const val MEASUREMENT_UNIT_NAME = "Kilogram"
private const val MEASUREMENT_UNIT_DIMENSION = "WEIGHT"
private const val QUANTITY = "1.5"
private const val UPDATED_QUANTITY = "2.5"

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

private fun createQuantifiedIngredientDocument(
    recipeId: String,
    ingredientId: String,
    measurementUnitId: String,
) = """
    mutation {
        createQuantifiedIngredient(creation: {
            recipeId: "$recipeId",
            ingredientId: "$ingredientId",
            measurementUnitId: "$measurementUnitId",
            quantity: $QUANTITY,
        }) {
            id
        }
    }
"""

private const val LIST_QUANTIFIED_INGREDIENTS_DOCUMENT = """
    query {
        quantifiedIngredients {
            edges {
                cursor
                node {
                    id
                    quantity
                }
            }
            pageInfo {
                hasNextPage
            }
        }
    }
"""

private const val GET_QUANTIFIED_INGREDIENT_DOCUMENT = $$"""
    query GetQuantifiedIngredient($id: ID!) {
        quantifiedIngredient(id: $id) {
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
            recipe {
                id
            }
        }
    }
"""

private const val UPDATE_QUANTIFIED_INGREDIENT_DOCUMENT = $$"""
    mutation UpdateQuantifiedIngredient($id: ID!) {
        updateQuantifiedIngredient(modification: {
            id: $id,
            quantity: 2.5,
        }) {
            id
            quantity
        }
    }
"""

private const val DELETE_QUANTIFIED_INGREDIENT_DOCUMENT = $$"""
    mutation RemoveQuantifiedIngredient($id: ID!) {
 	    deleteQuantifiedIngredient(id: $id)
    }
"""

@SpringBootTest(classes = [TestApplication::class])
@AutoConfigureMockMvc
class QuantifiedIngredientControllerTest : ControllerTestBase {

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

    private fun createRecipeId(): String =
        tester.document(createRecipeDocument(createDishId(), createUsername()))
            .execute()
            .path("data.createRecipe.id")
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

    private fun createQuantifiedIngredientId(
        recipeId: String,
        ingredientId: String,
        measurementUnitId: String,
    ): String =
        tester.document(createQuantifiedIngredientDocument(recipeId, ingredientId, measurementUnitId))
            .execute()
            .path("data.createQuantifiedIngredient.id")
            .hasValue()
            .entity(String::class.java)
            .get()

    @Test
    fun quantifiedIngredient() {
        val recipeId = createRecipeId()
        val ingredientId = createIngredientId()
        val measurementUnitId = createMeasurementUnitId()
        val createdId = createQuantifiedIngredientId(recipeId, ingredientId, measurementUnitId)

        tester.document(GET_QUANTIFIED_INGREDIENT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.quantifiedIngredient") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("quantity").entity(Double::class.java).isEqualTo(QUANTITY.toDouble())
                    .path("ingredient.id").entity(String::class.java).isEqualTo(ingredientId)
                    .path("ingredient.name").entity(String::class.java).isEqualTo(INGREDIENT_NAME)
                    .path("unit.id").entity(String::class.java).isEqualTo(measurementUnitId)
                    .path("unit.name").entity(String::class.java).isEqualTo(MEASUREMENT_UNIT_NAME)
                    .path("recipe.id").entity(String::class.java).isEqualTo(recipeId)
            }
    }

    @Test
    fun createQuantifiedIngredient() {
        tester.document(
            createQuantifiedIngredientDocument(createRecipeId(), createIngredientId(), createMeasurementUnitId())
        )
            .execute()
            .path("data.createQuantifiedIngredient.id")
            .hasValue()
    }

    @Test
    fun quantifiedIngredients() {
        val createdId = createQuantifiedIngredientId(createRecipeId(), createIngredientId(), createMeasurementUnitId())
        tester.document(LIST_QUANTIFIED_INGREDIENTS_DOCUMENT)
            .execute()
            .path("data.quantifiedIngredients.edges[*].node.id")
            .entityList(String::class.java)
            .contains(createdId)
    }

    @Test
    fun createQuantifiedIngredientWithUnknownIngredientFails() {
        tester.document(
            createQuantifiedIngredientDocument(createRecipeId(), "unknown-ingredient-id", createMeasurementUnitId())
        )
            .execute()
            .errors()
            .expect { it.errorType.toString() == "NOT_FOUND" }
            .verify()
    }

    @Test
    fun updateQuantifiedIngredient() {
        val createdId = createQuantifiedIngredientId(createRecipeId(), createIngredientId(), createMeasurementUnitId())
        tester.document(UPDATE_QUANTIFIED_INGREDIENT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.updateQuantifiedIngredient") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("quantity").entity(Double::class.java).isEqualTo(UPDATED_QUANTITY.toDouble())
            }
        tester.document(GET_QUANTIFIED_INGREDIENT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.quantifiedIngredient.quantity")
            .entity(Double::class.java)
            .isEqualTo(UPDATED_QUANTITY.toDouble())
    }

    @Test
    fun deleteQuantifiedIngredient() {
        val createdId = createQuantifiedIngredientId(createRecipeId(), createIngredientId(), createMeasurementUnitId())
        tester.document(DELETE_QUANTIFIED_INGREDIENT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.deleteQuantifiedIngredient")
            .entity(Boolean::class.java)
            .isEqualTo(true)
        tester.document(GET_QUANTIFIED_INGREDIENT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "NOT_FOUND" }
            .verify()
    }
}
