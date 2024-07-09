package co.digamma.ca.persistence.sql.media

import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.domain.spi.media.ImageRepository
import co.digamma.ca.persistence.jooq.media.tables.records.ImageRecord
import co.digamma.ca.persistence.jooq.media.tables.references.IMAGE
import co.digamma.ca.persistence.sql.SqlCrudRepository
import org.jooq.DSLContext

class SqlImageRepository(dsl: DSLContext):
    SqlCrudRepository<Image, ImageRecord>(IMAGE, IMAGE.ID, dsl, Image::class),
    ImageRepository {

    override fun create(model: Image): Image {
        dsl.insertInto(IMAGE)
            .set(IMAGE.ID, model.id)
            .set(IMAGE.LOCALE, model.locale.toString())
            .set(IMAGE.URL, model.url)
            .set(IMAGE.DESCRIPTION, model.description)
            .execute()
        return model
    }

    override fun update(model: Image): Image {
        dsl.update(IMAGE)
            .set(IMAGE.LOCALE, model.locale.toString())
            .set(IMAGE.URL, model.url)
            .set(IMAGE.DESCRIPTION, model.description)
            .where(IMAGE.ID.eq(model.id))
            .execute()
        return model
    }
}