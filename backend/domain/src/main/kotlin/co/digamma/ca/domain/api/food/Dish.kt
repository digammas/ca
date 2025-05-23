package co.digamma.ca.domain.api.food

import co.digamma.ca.domain.api.media.Images
import co.digamma.ca.domain.api.media.noImages
import co.digamma.ca.domain.api.model.LocalizedModel
import java.util.Locale

data class Dish(
    override val id: String,
    override val locale: Locale,
    val name: String,
    val course: Course,
    val cuisine: Cuisine,
    val serving: Serving,
    val images: Images = noImages(),
) : LocalizedModel
