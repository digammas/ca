package co.digamma.ca.suites.media

import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.domain.spi.media.ImageRepository
import co.digamma.ca.fixtures.givenLocale
import co.digamma.ca.fixtures.givenString
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase

abstract class ImageRepositoryTestBase : CrudRepositoryTestBase<Image>() {

    abstract override val sut: ImageRepository

    override fun newModel() = givenImage()

    override fun modifyModel(model: Image): Image = model.copy(
        locale = givenLocale(),
        url = "https://example.com/${givenString(12)}.png",
        description = givenString(),
    )
}