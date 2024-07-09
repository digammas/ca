package co.digamma.ca.persistence.sql.media

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.domain.spi.media.ImageRepository
import co.digamma.ca.persistence.jooq.media.tables.references.IMAGE
import org.jooq.DSLContext

class SqlImageRepository(
    private val dsl: DSLContext
): ImageRepository {

    override fun retrieve(id: String): Image? {
        return dsl.selectFrom(IMAGE).where(IMAGE.ID.eq(id)).fetchOneInto(Image::class.java)
    }

    override fun retrieve(pageSpecs: PageSpecs): Page<Image> {
        val total = dsl.selectCount().from(IMAGE).fetchOne(0, Int::class.java)!!
        val list =  dsl.selectFrom(IMAGE)
            .offset(pageSpecs.index * pageSpecs.size)
            .limit(pageSpecs.size)
            .fetchInto(Image::class.java)
        return Page(list, pageSpecs.index, pageSpecs.size, total)
    }

    override fun retrieveAll(): List<Image> {
        return dsl.selectFrom(IMAGE).fetchInto(Image::class.java)
    }

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

    override fun delete(id: String) {
        dsl.deleteFrom(IMAGE).where(IMAGE.ID.eq(id)).execute()
    }
}