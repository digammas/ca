package co.digamma.ca.domain.api.model

import java.util.Locale

interface LocalizedModel: Model {
    val locale: Locale
}
