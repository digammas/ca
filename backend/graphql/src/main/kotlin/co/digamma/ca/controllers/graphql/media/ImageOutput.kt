package co.digamma.ca.controllers.graphql.media

import co.digamma.ca.domain.api.media.Image

data class ImageOutput(
    val id: String,
    val locale: String,
    val url: String,
    val description: String,
)

fun Image.toImageOutput() = ImageOutput(
    id = this.id,
    locale = this.locale.toString(),
    url = this.url,
    description = this.description,
)
