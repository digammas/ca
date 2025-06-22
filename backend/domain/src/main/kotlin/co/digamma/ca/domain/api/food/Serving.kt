package co.digamma.ca.domain.api.food

import co.digamma.ca.domain.api.model.LocalizedModel
import java.util.Locale

data class Serving(
    override val id: String,
    override val locale: Locale,
    val name: String,
) : LocalizedModel
