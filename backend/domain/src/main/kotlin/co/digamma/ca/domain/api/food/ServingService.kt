package co.digamma.ca.domain.api.food

import co.digamma.ca.domain.api.DeleteService
import co.digamma.ca.domain.api.RetrieveService
import java.util.Locale

interface ServingService: RetrieveService<Serving>, DeleteService {
    fun create(creation: ServingCreation): Serving
    fun update(modification: ServingModification): Serving
}

interface ServingMutation {
    val name: String?
    val temperature: IntRange?
}

data class ServingCreation(
    val locale: Locale,
    override val name: String,
    override val temperature: IntRange?,
): ServingMutation

data class ServingModification(
    val id: String,
    override val name: String?,
    override val temperature: IntRange?,
): ServingMutation