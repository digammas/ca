package co.digamma.ca.fixtures.inmem.media

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.domain.spi.media.ImageRepository
import co.digamma.ca.fixtures.inmem.InMemCrudRepository

@Singleton
class InMemImageRepository: InMemCrudRepository<Image>(), ImageRepository