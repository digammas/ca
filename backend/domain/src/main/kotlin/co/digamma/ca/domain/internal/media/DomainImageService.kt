package co.digamma.ca.domain.internal.media

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.domain.api.media.ImageService
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.spi.CrudRepository

@Singleton
class DomainImageService(
    override val repository: CrudRepository<Image>
) : DefaultCurdService<Image>(), ImageService