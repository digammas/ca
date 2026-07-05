package co.digamma.ca.domain.internal.media

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.domain.api.media.ImageCreation
import co.digamma.ca.domain.api.media.ImageModification
import co.digamma.ca.domain.api.media.ImageService
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.spi.media.ImageRepository

@Singleton
class DomainImageService(
    override val repository: ImageRepository
) : DefaultCurdService<Image, ImageCreation, ImageModification>(), ImageService {

    override fun toModel(creation: ImageCreation) = Image(
        id = generateId(),
        locale = creation.locale,
        url = creation.url,
        description = creation.description
    )

    override fun toModel(modification: ImageModification, existing: Image) = Image(
            id = existing.id,
            locale = existing.locale,
            url = modification.url ?: existing.url,
            description = modification.description ?: existing.description
        )
}