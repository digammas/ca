package co.digamma.ca.domain.internal.food

import co.digamma.ca.domain.api.food.Cuisine
import co.digamma.ca.domain.api.food.CuisineCreation
import co.digamma.ca.domain.api.food.CuisineModification
import co.digamma.ca.domain.api.food.CuisineService
import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.spi.CrudRepository

@Singleton
class DomainCuisineService(
    override val repository: CrudRepository<Cuisine>
) : DefaultCurdService<Cuisine>(), CuisineService {

    override fun create(creation: CuisineCreation): Cuisine {
        val cuisine = Cuisine(
            id = generateId(),
            locale = creation.locale,
            name = creation.name,
        )
        return this.repository.create(cuisine)
    }

    override fun update(modification: CuisineModification): Cuisine {
        val existing = this.retrieve(modification.id)
        val cuisine = Cuisine(
            id = existing.id,
            locale = existing.locale,
            name = modification.name ?: existing.name,
        )
        return this.repository.update(cuisine)
    }
}