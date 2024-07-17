package co.digamma.ca.domain.api.cookbook

import co.digamma.ca.domain.api.model.LocalizedModel
import java.util.Locale

class MeasurementUnit(
    override val id: String,
    override val locale: Locale,
    val name: String,
    val dimension: Dimension,
) : LocalizedModel {

    enum class Dimension {
        QUANTITY,
        VOLUME,
        WEIGHT,
    }
}

