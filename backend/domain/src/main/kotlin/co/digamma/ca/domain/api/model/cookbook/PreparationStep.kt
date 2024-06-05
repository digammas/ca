package co.digamma.ca.domain.api.model.cookbook

import co.digamma.ca.domain.api.model.LocalizedModel
import java.util.Locale

class PreparationStep(
    override val id: String,
    override val locale: Locale,
    val description: String,
): LocalizedModel
