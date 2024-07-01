package co.digamma.ca.domain.api.food

import co.digamma.ca.domain.api.CrudService
import java.util.Locale

interface CuisineService: CrudService<Cuisine, CuisineCreation, CuisineModification>

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

