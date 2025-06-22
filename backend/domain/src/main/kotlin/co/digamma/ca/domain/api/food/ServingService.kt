package co.digamma.ca.domain.api.food

import co.digamma.ca.domain.api.CrudService
import java.util.Locale

interface ServingService : CrudService<Serving, ServingCreation, ServingModification>

interface ServingMutation {
    val name: String?
}

data class ServingCreation(
    val locale: Locale,
    override val name: String,
) : ServingMutation

data class ServingModification(
    val id: String,
    override val name: String?,
) : ServingMutation
