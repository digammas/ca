package co.digamma.ca.domain.internal.cookbook

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.cookbook.Ingredient
import co.digamma.ca.domain.api.cookbook.IngredientCreation
import co.digamma.ca.domain.api.cookbook.IngredientModification
import co.digamma.ca.domain.api.cookbook.IngredientService
import co.digamma.ca.domain.api.media.Images
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.internal.retrieveOrThrow
import co.digamma.ca.domain.spi.cookbook.IngredientRepository
import co.digamma.ca.domain.spi.media.ImageRepository

@Singleton
open class DomainIngredientService(
    override val repository: IngredientRepository,
    private val imageRepository: ImageRepository,
) : DefaultCurdService<Ingredient>(), IngredientService {

    override fun create(creation: IngredientCreation): Ingredient {
        val model = Ingredient(
            id = generateId(),
            locale = creation.locale,
            name = creation.name,
            description = creation.description,
            images = Images(creation.imageIds.map(imageRepository::retrieveOrThrow)),
        )
        return repository.create(model)
    }

    override fun update(modification: IngredientModification): Ingredient {
        val existing = this.retrieve(modification.id)
        val model = Ingredient(
            id = existing.id,
            locale = existing.locale,
            name = modification.name ?: existing.name,
            description = modification.description ?: existing.description,
            images = modification.imageIds
                ?.map(imageRepository::retrieveOrThrow)
                ?.let(::Images)
                ?: existing.images,
        )
        return repository.update(model)
    }
}

