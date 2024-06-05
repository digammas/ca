package co.digamma.ca.domain.api.model.food

import co.digamma.ca.domain.api.model.LocalizedModel
import java.util.Locale

class Course(
    override val id: String,
    override val locale: Locale,
    val name: String,
): LocalizedModel
