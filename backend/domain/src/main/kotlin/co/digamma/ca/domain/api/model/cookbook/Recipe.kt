package co.digamma.ca.domain.api.model.cookbook

import co.digamma.ca.domain.api.model.LocalizedModel
import co.digamma.ca.domain.api.model.food.Dish
import co.digamma.ca.domain.api.model.users.User
import sun.jvm.hotspot.oops.Instance
import java.util.Locale

class Recipe(
    override val id: String,
    override val locale: Locale,
    val dish: Dish,
    val preparation: List<PreparationStep>,
    val yield: Int,
    val createdAt: Instance,
    val updatedAt: Instance,
    val author: User,
): LocalizedModel
