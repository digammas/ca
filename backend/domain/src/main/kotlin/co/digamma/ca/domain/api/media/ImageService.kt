package co.digamma.ca.domain.api.media

import co.digamma.ca.domain.api.CreateService
import co.digamma.ca.domain.api.DeleteService
import co.digamma.ca.domain.api.UpdateService
import java.util.Locale

interface ImageService:
    CreateService<Image, ImageCreation>,
    UpdateService<Image, ImageModification>,
    DeleteService {
    fun retrieve(id: String): Image
}

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
