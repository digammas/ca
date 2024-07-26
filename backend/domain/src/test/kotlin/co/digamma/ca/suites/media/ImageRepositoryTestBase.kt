package co.digamma.ca.suites.media

import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.domain.spi.media.ImageRepository
import co.digamma.ca.fixtures.utils.RandGen
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase
import java.util.Locale

abstract class ImageRepositoryTestBase : CrudRepositoryTestBase<Image>() {

    abstract override val sut: ImageRepository

    override fun newModel(): Image = Image(
        id = RandGen.uuid(),
        locale = Locale.of("en"),
        url = "https://example.com/image.png",
        description = "Image description"
    )

    override fun modifyModel(model: Image): Image = model.copy(
        locale = Locale.of("fr"),
        url = "https://example.fr/image.png",
        description = "Description de l'image"
    )
}