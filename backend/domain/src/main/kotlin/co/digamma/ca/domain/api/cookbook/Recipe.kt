package co.digamma.ca.domain.api.cookbook

import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.api.media.Images
import co.digamma.ca.domain.api.media.noImages
import co.digamma.ca.domain.api.model.LocalizedModel
import co.digamma.ca.domain.api.users.User
import java.time.Instant
import java.util.Locale

class Recipe(
    override val id: String,
    override val locale: Locale,
    val dish: Dish,
    val ingredients: List<QuantifiedIngredient>,
    val steps: List<PreparationStep>,
    val yield: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
    val author: User,
    val images: Images = noImages(),
    val estimatedTime: Int = steps.sumOf { it.estimatedTime },
) : LocalizedModel
