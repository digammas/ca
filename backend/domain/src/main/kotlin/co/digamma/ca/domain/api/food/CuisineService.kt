package co.digamma.ca.domain.api.food

import co.digamma.ca.domain.api.DeleteService
import co.digamma.ca.domain.api.RetrieveService
import java.util.Locale

interface CuisineService: RetrieveService<Cuisine>, DeleteService {
    fun create(creation: CuisineCreation): Cuisine
    fun update(modification: CuisineModification): Cuisine
}

interface CuisineMutation {
    val name: String?
}

data class CuisineCreation(
    val locale: Locale,
    override val name: String,
): CuisineMutation

data class CuisineModification(
    val id: String,
    override val name: String?,
): CuisineMutation

