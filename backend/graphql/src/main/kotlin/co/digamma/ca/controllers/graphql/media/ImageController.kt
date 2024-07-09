package co.digamma.ca.controllers.graphql.media

import co.digamma.ca.domain.api.media.ImageService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller("/graphql")
class ImageController(private val service: ImageService) {

    @QueryMapping
    fun image(@Argument id: String): ImageOutput {
        return service.retrieve(id).toImageOutput()
    }

    @MutationMapping
    fun deleteImage(@Argument id: String): Boolean {
        service.delete(id)
        return true
    }

    @MutationMapping
    fun createImage(@Argument creation: ImageCreationInput): ImageOutput {
        return service.create(creation.toImageCreation()).toImageOutput()
    }
}
