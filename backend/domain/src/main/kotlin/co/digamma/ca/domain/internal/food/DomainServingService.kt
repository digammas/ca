package co.digamma.ca.domain.internal.food

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.food.Serving
import co.digamma.ca.domain.api.food.ServingCreation
import co.digamma.ca.domain.api.food.ServingModification
import co.digamma.ca.domain.api.food.ServingService
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.spi.food.ServingRepository

@Singleton
open class DomainServingService(
    override val repository: ServingRepository
) : DefaultCurdService<Serving, ServingCreation, ServingModification>(), ServingService {

    override fun toModel(creation: ServingCreation) = Serving(
        id = generateId(),
        locale = creation.locale,
        name = creation.name,
    )

    override fun toModel(modification: ServingModification, existing: Serving) = Serving(
            id = existing.id,
            locale = existing.locale,
            name = modification.name ?: existing.name,
        )
}
