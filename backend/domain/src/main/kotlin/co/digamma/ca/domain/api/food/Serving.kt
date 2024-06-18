package co.digamma.ca.domain.api.food

import co.digamma.ca.domain.api.model.LocalizedModel
import java.util.*

class Serving(
    override val id: String,
    override val locale: Locale,
    val name: String,
    val temperature: IntRange,
): LocalizedModel
