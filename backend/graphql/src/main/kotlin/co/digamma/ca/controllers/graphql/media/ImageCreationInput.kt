package co.digamma.ca.controllers.graphql.media

import co.digamma.ca.domain.api.media.ImageCreation
import java.util.*

class ImageCreationInput(
    private val locale: String,
    private val url: String,
    private val description: String
) {

    fun toImageCreation() = ImageCreation(
        locale = Locale.forLanguageTag(locale),
        url = url,
        description = description
    )

}