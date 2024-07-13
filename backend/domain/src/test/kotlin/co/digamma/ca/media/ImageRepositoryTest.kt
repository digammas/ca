package co.digamma.ca.media

import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.domain.spi.media.ImageRepository
import co.digamma.ca.persistence.CrudRepositoryTest
import java.util.Locale
import java.util.UUID

abstract class ImageRepositoryTest(
    sut: ImageRepository
) : CrudRepositoryTest<Image>(sut) {

    override fun newModel(): Image = Image(
        id = UUID.randomUUID().toString(),
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