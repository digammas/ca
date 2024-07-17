package co.digamma.ca.domain.api.cookbook

import co.digamma.ca.domain.api.model.LocalizedModel
import co.digamma.ca.domain.api.media.Images
import co.digamma.ca.domain.api.media.noImages
import java.util.Locale

class Ingredient(
    override val id: String,
    override val locale: Locale,
    val name: String,
    val description: String,
    val images: Images = noImages(),
) : LocalizedModel
