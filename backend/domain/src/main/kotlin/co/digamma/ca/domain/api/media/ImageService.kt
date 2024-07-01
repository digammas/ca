package co.digamma.ca.domain.api.media

import co.digamma.ca.domain.api.CrudService
import java.util.Locale

interface ImageService: CrudService<Image, ImageCreation, ImageModification>

interface ImageMutation {
    val url: String?
    val description: String?
}

data class ImageCreation(
    val locale: Locale,
    override val url: String,
    override val description: String
): ImageMutation

data class ImageModification(
    val id: String,
    override val url: String?,
    override val description: String?
): ImageMutation
