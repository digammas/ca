package co.digamma.ca.domain.internal.food

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.food.Cuisine
import co.digamma.ca.domain.api.food.CuisineCreation
import co.digamma.ca.domain.api.food.CuisineModification
import co.digamma.ca.domain.api.food.CuisineService
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.spi.food.CuisineRepository

@Singleton
open class DomainCuisineService(
    override val repository: CuisineRepository
) : DefaultCurdService<Cuisine, CuisineCreation, CuisineModification>(), CuisineService {

    override fun toModel(creation: CuisineCreation) = Cuisine(
        id = generateId(),
        locale = creation.locale,
        name = creation.name,
    )

    override fun toModel(modification: CuisineModification, existing: Cuisine) = Cuisine(
            id = existing.id,
            locale = existing.locale,
            name = modification.name ?: existing.name,
        )
}
