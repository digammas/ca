package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.ControllerTestBase
import co.digamma.ca.TestApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

private const val LOCALE = "en"
private const val NAME = "Cup"
private const val DIMENSION = "VOLUME"
private const val UPDATED_NAME = "Gram"
private const val UPDATED_DIMENSION = "WEIGHT"

private const val CREATE_MEASUREMENT_UNIT_DOCUMENT = """
    mutation {
        createMeasurementUnit(creation: {
            locale: "$LOCALE",
            name: "$NAME",
            dimension: $DIMENSION,
        }) {
            id
        }
    }
"""

private const val GET_MEASUREMENT_UNIT_DOCUMENT = $$"""
    query GetMeasurementUnit($id: ID!) {
        measurementUnit(id: $id) {
            id
            locale
            name
            dimension
        }
    }
"""

private const val UPDATE_MEASUREMENT_UNIT_DOCUMENT = $$"""
    mutation UpdateMeasurementUnit($id: ID!) {
        updateMeasurementUnit(modification: {
            id: $id,
            name: "Gram",
            dimension: WEIGHT,
        }) {
            id
            locale
            name
            dimension
        }
    }
"""

private const val DELETE_MEASUREMENT_UNIT_DOCUMENT = $$"""
    mutation RemoveMeasurementUnit($id: ID!) {
 	    deleteMeasurementUnit(id: $id)
    }
"""

@SpringBootTest(classes = [TestApplication::class])
@AutoConfigureMockMvc
class MeasurementUnitControllerTest : ControllerTestBase {

    @Autowired
    override lateinit var mockMvc: MockMvc

    private fun createMeasurementUnitId(): String =
        tester.document(CREATE_MEASUREMENT_UNIT_DOCUMENT)
            .execute()
            .path("data.createMeasurementUnit.id")
            .hasValue()
            .entity(String::class.java)
            .get()

    @Test
    fun measurementUnit() {
        val createdId = createMeasurementUnitId()
        tester.document(GET_MEASUREMENT_UNIT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.measurementUnit") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("locale").entity(String::class.java).isEqualTo(LOCALE)
                    .path("name").entity(String::class.java).isEqualTo(NAME)
                    .path("dimension").entity(String::class.java).isEqualTo(DIMENSION)
            }
    }

    @Test
    fun createMeasurementUnit() {
        tester.document(CREATE_MEASUREMENT_UNIT_DOCUMENT)
            .execute()
            .path("data.createMeasurementUnit.id")
            .hasValue()
    }

    @Test
    fun updateMeasurementUnit() {
        val createdId = createMeasurementUnitId()
        tester.document(UPDATE_MEASUREMENT_UNIT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.updateMeasurementUnit") {
                it
                    .path("id").entity(String::class.java).isEqualTo(createdId)
                    .path("locale").entity(String::class.java).isEqualTo(LOCALE)
                    .path("name").entity(String::class.java).isEqualTo(UPDATED_NAME)
                    .path("dimension").entity(String::class.java).isEqualTo(UPDATED_DIMENSION)
            }
        tester.document(GET_MEASUREMENT_UNIT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.measurementUnit.dimension")
            .entity(String::class.java)
            .isEqualTo(UPDATED_DIMENSION)
    }

    @Test
    fun deleteMeasurementUnit() {
        val createdId = createMeasurementUnitId()
        tester.document(DELETE_MEASUREMENT_UNIT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .path("data.deleteMeasurementUnit")
            .entity(Boolean::class.java)
            .isEqualTo(true)
        tester.document(GET_MEASUREMENT_UNIT_DOCUMENT)
            .variables(mapOf("id" to createdId))
            .execute()
            .errors()
            .expect { it.errorType.toString() == "NOT_FOUND" }
            .verify()
    }
}
