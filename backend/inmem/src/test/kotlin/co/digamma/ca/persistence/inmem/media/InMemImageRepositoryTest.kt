package co.digamma.ca.persistence.inmem.media

import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.media.ImageRepositoryTest
import java.util.Locale

class InMemImageRepositoryTest : ImageRepositoryTest() {

    override val sut = InMemImageRepository()

    override fun newModel(): Image = Image(
        id = "id",
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