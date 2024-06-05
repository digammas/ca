package co.digamma.ca.domain.api.model.cookbook

import java.util.Locale

class MeasurementUnit(
    val id: String,
    val locale: Locale,
    val name: String,
    val dimension: Dimension,
) {

    enum class Dimension {
        QUANTITY,
        VOLUME,
        WEIGHT,
    }
}

