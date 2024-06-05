package co.digamma.ca.domain.api.model.food

import co.digamma.ca.domain.api.model.LocalizedModel
import java.util.Locale

class Dish(
    override val id: String,
    override val locale: Locale,
    val course: Course,
    val cuisine: Cuisine,
    val serving: Serving,
    val sideDishes: List<Dish> = emptyList(),
): LocalizedModel
