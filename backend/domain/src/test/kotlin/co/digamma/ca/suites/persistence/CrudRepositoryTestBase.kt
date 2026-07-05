package co.digamma.ca.suites.persistence

import co.digamma.ca.domain.api.common.DuplicateKeyException
import co.digamma.ca.domain.api.common.NotFoundException
import co.digamma.ca.domain.api.model.Model
import co.digamma.ca.domain.spi.CrudRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

abstract class CrudRepositoryTestBase<T : Model> {
    abstract fun newModel(): T
    abstract fun modifyModel(model: T): T
    abstract val sut: CrudRepository<T>

    @Test
    fun `test retrieve`() {
        val model = newModel()
        sut.create(model)
        val retrieved = sut.retrieve(model.id)
        assertSame(model, retrieved)
    }

    @Test
    fun `test retrieve not found`() {
        assertNull(sut.retrieve("not-found"))
    }

    @Test
    fun `test create`() {
        val model = newModel()
        val created = sut.create(model)
        assertSame(model, created)
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
        assertSame(expected, modified)
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

    protected open fun normalize(model: T): T = model

    private fun assertSame(expected: T, actual: T?) {
        assertNotNull(actual)
        assertEquals(normalize(expected), normalize(actual))
    }
}