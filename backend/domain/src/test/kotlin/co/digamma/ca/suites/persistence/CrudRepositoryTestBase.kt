package co.digamma.ca.suites.persistence

import co.digamma.ca.domain.api.common.DuplicateKeyException
import co.digamma.ca.domain.api.common.NotFoundException
import co.digamma.ca.domain.api.model.Model
import co.digamma.ca.domain.spi.CrudRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

abstract class CrudRepositoryTestBase<T : Model> {
    abstract fun newModel(): T
    abstract fun modifyModel(model: T): T
    abstract val sut: CrudRepository<T>

    @Test
    fun `test retrieve`() {
        val model = newModel()
        sut.create(model)
        val retrieved = sut.retrieve(model.id)
        assertEquals(model, retrieved)
    }

    @Test
    fun `test retrieve not found`() {
        assertNull(sut.retrieve("not-found"))
    }

    @Test
    fun `test create`() {
        val model = newModel()
        val created = sut.create(model)
        assertEquals(model, created)
    }

    @Test
    fun `test create duplicate fail`() {
        val model = newModel()
        sut.create(model)
        assertThrows(DuplicateKeyException::class.java) {
            sut.create(model)
        }
    }

    @Test
    fun `test update`() {
        val model = newModel()
        val created = sut.create(model)
        val expected = modifyModel(created)
        sut.update(expected)
        val modified = sut.retrieve(model.id)
        assertEquals(expected, modified)
    }

    @Test
    fun `test update not found`() {
        val model = newModel()
        assertThrows(NotFoundException::class.java) {
            sut.update(model)
        }
    }

    @Test
    fun `test delete`() {
        val model = newModel()
        sut.create(model)
        sut.delete(model.id)
        assertNull(sut.retrieve(model.id))
    }
}