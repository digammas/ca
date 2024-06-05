package co.digamma.ca.domain.api.cookbook

import co.digamma.ca.domain.api.model.LocalizedModel
import co.digamma.ca.domain.api.common.Images
import co.digamma.ca.domain.api.common.noImages
import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.api.users.User
import sun.jvm.hotspot.oops.Instance
import java.util.Locale

class Recipe(
    override val id: String,
    override val locale: Locale,
    val dish: Dish,
    val steps: List<PreparationStep>,
    val yield: Int,
    val createdAt: Instance,
    val updatedAt: Instance,
    val author: User,
    val images: Images = noImages(),
    val estimatedTime: Int = steps.sumOf { it.estimatedTime },
): LocalizedModel
