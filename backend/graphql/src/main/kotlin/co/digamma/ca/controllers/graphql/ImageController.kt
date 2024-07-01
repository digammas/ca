package co.digamma.ca.controllers.graphql

import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.domain.api.media.ImageCreation
import co.digamma.ca.domain.api.media.ImageService
import graphql.relay.Connection
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.Arguments
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller("/graphql")
class ImageController(private val service: ImageService) {

    @QueryMapping
    fun image(@Argument id: String): Image {
        return service.retrieve(id)
    }

    @QueryMapping
    fun images(@Arguments pageQuery: PageQuery): Connection<Image> {
        return service.retrieve(pageQuery.asPageSpecs()).asConnection()
    }

    @MutationMapping
    fun deleteImage(@Argument id: String): Boolean {
        service.delete(id)
        return true
    }

    @MutationMapping
    fun createImage(@Argument creation: ImageCreation): Image {
        return service.create(creation)
    }
}
