package co.digamma.ca.domain.api.media

import co.digamma.ca.domain.api.model.LocalizedModel
import java.util.Locale

data class Image(
    override val id: String,
    override val locale: Locale,
    val url: String,
    val description: String,
) : LocalizedModel

data class Images(
    val images: List<Image>
) : List<Image> by images {
    val principal: Image? = firstOrNull()
}

fun noImages(): Images = Images(emptyList())
