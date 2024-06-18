package co.digamma.ca.domain.api.media

import co.digamma.ca.domain.api.model.LocalizedModel
import java.util.Locale

class Image(
    override val id: String,
    override val locale: Locale,
    val url: String,
    val description: String,
): LocalizedModel

class Images(
    images: List<Image>
): List<Image> by images {
    fun principal(): Image? = firstOrNull()
}

fun noImages(): Images = Images(emptyList())
