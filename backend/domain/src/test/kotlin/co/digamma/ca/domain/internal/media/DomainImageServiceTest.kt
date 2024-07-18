package co.digamma.ca.domain.internal.media

import co.digamma.ca.domain.api.common.NotFoundException
import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.domain.api.media.ImageCreation
import co.digamma.ca.domain.api.media.ImageModification
import co.digamma.ca.fixtures.inmem.media.InMemImageRepository
import co.digamma.ca.fixtures.utils.RandGen
import java.util.Locale
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DomainImageServiceTest {

    private val repository = InMemImageRepository()
    private val sut = DomainImageService(repository)

    @Test
    fun `test create`() {
        val description = RandGen.string()
        val creation = ImageCreation(
            Locale.of("en"),
            "https://www.example.com/img.png",
            description
        )
        sut.create(creation)
        val image: Image? = repository.retrieveAll().find { it.description == description }
        assertNotNull(image)
        assertEquals(creation.url, image?.url)
        assertEquals(creation.locale, image?.locale)
    }

    @Test
    fun `test retrieve by ID`() {
        val id = UUID.randomUUID().toString()
        val image = Image(
            id = id,
            locale = Locale.ENGLISH,
            url = "https://www.example.com/img.png",
            description = RandGen.string()
        )
        repository.create(image)
        val retrieved = sut.retrieve(id)
        assertEquals(image, retrieved)
    }

    @Test
    fun `test retrieve all`() {
        val list = List(15) {
            Image(
                id = UUID.randomUUID().toString(),
                locale = Locale.ENGLISH,
                url = "https://www.example.com/img.png",
                description = RandGen.string()
            )
        }
        list.forEach(repository::create)
        val retrieved = sut.retrieve()
        assertTrue(list.all(retrieved::contains))
    }

    @Test
    fun `test delete`() {
        val creation = ImageCreation(
            Locale.of("en"),
            "https://www.example.com/img.png",
            RandGen.string()
        )
        val created = sut.create(creation)
        sut.delete(created.id)
        assertThrows(NotFoundException::class.java) {
            sut.retrieve(created.id)
        }
    }

    @Test
    fun update() {
        val creation = ImageCreation(
            Locale.of("en"),
            "https://www.example.com/img.png",
            RandGen.string()
        )
        val created = sut.create(creation)
        val update = ImageModification(
            id = created.id,
            url = created.url,
            description = RandGen.string()
        )
        sut.update(update)
        val retrieved = sut.retrieve(created.id)
        assertEquals(update.description, retrieved.description)
        assertEquals(creation.url, retrieved.url)
        assertEquals(creation.locale, retrieved.locale)
    }

}