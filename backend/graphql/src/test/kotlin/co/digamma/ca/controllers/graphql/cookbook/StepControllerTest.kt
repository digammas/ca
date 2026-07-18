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
private const val DESCRIPTION = "Roast the chicken in the oven"
private const val ESTIMATED_TIME = 75
private const val UPDATED_DESCRIPTION = "Roast the chicken on the grill"
private const val UPDATED_ESTIMATED_TIME = 60

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

private fun createStepDocument(recipeId: String) = """
    mutation {
        createStep(creation: {
            recipeId: "$recipeId",
            description: "$DESCRIPTION",
            estimatedTime: $ESTIMATED_TIME,
        }) {
            id
        }
    }
"""

private const val GET_STEP_DOCUMENT = $$"""
    query GetStep($id: ID!) {
        step(id: $id) {
            id
            locale
            description
            estimatedTime
            recipe {
                id
            }
        }
    }
"""

private const val UPDATE_STEP_DOCUMENT = $$"""
    mutation UpdateStep($id: ID!) {
        updateStep(modification: {
            id: $id,
            description: "Roast the chicken on the grill",
            estimatedTime: 60,
        }) {
            id
            locale
            description
            estimatedTime
        }
    }
"""

private const val DELETE_STEP_DOCUMENT = $$"""
    mutation RemoveStep($id: ID!) {
 	    deleteStep(id: $id)
    }
"""

@SpringBootTest(classes = [TestApplication::class])
@AutoConfigureMockMvc
class StepControllerTest : ControllerTestBase {

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

    private fun createStepId(recipeId: String): String =
        tester.document(createStepDocument(recipeId))
            .execute()
            .path("data.createStep.id")
            .hasValue()
            .entity(String::class.java)
            .get()

    @Test
    fun step() {
        val recipeId = createRecipeId()
        val createdId = createStepId(recipeId)
        tester.document(GET_STEP_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.step") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("locale").entity(String::class.java).isEqualTo(LOCALE)
                    .path("description").entity(String::class.java).isEqualTo(DESCRIPTION)
                    .path("estimatedTime").entity(Int::class.java).isEqualTo(ESTIMATED_TIME)
                    .path("recipe.id").entity(String::class.java).isEqualTo(recipeId)
            }
    }

    @Test
    fun createStep() {
        tester.document(createStepDocument(createRecipeId()))
            .execute()
            .path("data.createStep.id")
            .hasValue()
    }

    @Test
    fun createStepWithUnknownRecipeFails() {
        tester.document(createStepDocument("unknown-recipe-id"))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "NOT_FOUND" }
            .verify()
    }

    @Test
    fun updateStep() {
        val createdId = createStepId(createRecipeId())
        tester.document(UPDATE_STEP_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.updateStep") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("locale").entity(String::class.java).isEqualTo(LOCALE)
                    .path("description").entity(String::class.java).isEqualTo(UPDATED_DESCRIPTION)
                    .path("estimatedTime").entity(Int::class.java).isEqualTo(UPDATED_ESTIMATED_TIME)
            }
        tester.document(GET_STEP_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.step.description")
            .entity(String::class.java)
            .isEqualTo(UPDATED_DESCRIPTION)
    }

    @Test
    fun deleteStep() {
        val createdId = createStepId(createRecipeId())
        tester.document(DELETE_STEP_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.deleteStep")
            .entity(Boolean::class.java)
            .isEqualTo(true)
        tester.document(GET_STEP_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "NOT_FOUND" }
            .verify()
    }
}
