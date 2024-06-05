package co.digamma.ca.domain.api.model.cookbook

import co.digamma.ca.domain.api.model.LocalizedModel
import java.util.Locale

class Ingredient(
    override val id: String,
    override val locale: Locale,
    val name: String,
    val description: String,
): LocalizedModel
