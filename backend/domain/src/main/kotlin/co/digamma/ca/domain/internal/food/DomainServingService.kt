package co.digamma.ca.domain.internal.food

import co.digamma.ca.domain.api.food.Serving
import co.digamma.ca.domain.api.food.ServingCreation
import co.digamma.ca.domain.api.food.ServingModification
import co.digamma.ca.domain.api.food.ServingService
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.spi.CrudRepository

class DomainServingService(
    override val repository: CrudRepository<Serving>
) : DefaultCurdService<Serving>(), ServingService {

    override fun create(creation: ServingCreation): Serving {
        val serving = Serving(
            id = generateId(),
            locale = creation.locale,
            name = creation.name,
            temperature = creation.temperature ?: IntRange.EMPTY,
        )
        return this.repository.create(serving)
    }

    override fun update(modification: ServingModification): Serving {
        val existing = this.retrieve(modification.id)
        val serving = Serving(
            id = existing.id,
            locale = existing.locale,
            name = modification.name ?: existing.name,
            temperature = modification.temperature ?: existing.temperature,
        )
        return this.repository.update(serving)
    }
}