package co.digamma.ca.domain.internal.media

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.domain.api.media.ImageCreation
import co.digamma.ca.domain.api.media.ImageModification
import co.digamma.ca.domain.api.media.ImageService
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.spi.CrudRepository

@Singleton
class DomainImageService(
    override val repository: CrudRepository<Image>
) : DefaultCurdService<Image>(), ImageService {

    override fun create(creation: ImageCreation): Image {
        val image = Image(
            id = generateId(),
            locale = creation.locale,
            url = creation.url,
            description = creation.description
        )
        return repository.create(image)
    }

    override fun update(modification: ImageModification): Image {
        val existing = this.retrieve(modification.id)
        val image = Image(
            id = existing.id,
            locale = existing.locale,
            url = modification.url ?: existing.url,
            description = modification.description ?: existing.description
        )
        return repository.update(image)
    }
}