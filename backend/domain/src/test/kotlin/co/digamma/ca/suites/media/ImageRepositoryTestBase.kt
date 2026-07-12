package co.digamma.ca.suites.media

import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.domain.spi.media.ImageRepository
import co.digamma.ca.fixtures.utils.givenLocale
import co.digamma.ca.fixtures.utils.givenString
import co.digamma.ca.fixtures.utils.media.givenImage
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase
import java.util.Locale

abstract class ImageRepositoryTestBase : CrudRepositoryTestBase<Image>() {

    abstract override val sut: ImageRepository

    override fun newModel() = givenImage()

    override fun modifyModel(model: Image): Image = model.copy(
        locale = givenLocale(),
        url = "https://example.com/${givenString(12)}.png",
        description = givenString(),
    )
}