package co.digamma.ca.persistence.sql.media

import co.digamma.ca.domain.api.common.utils.LoggerFactory
import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.domain.spi.media.ImageRepository
import co.digamma.ca.persistence.jooq.media.tables.records.ImageRecord
import co.digamma.ca.persistence.jooq.media.tables.references.IMAGE
import co.digamma.ca.persistence.sql.SqlCrudRepository
import java.util.Locale
import java.util.logging.Logger
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository

fun toImage(record: Record): Image {
    return Image(
        id = record[IMAGE.ID]!!,
        locale = Locale.of(record[IMAGE.LOCALE]),
        description = record[IMAGE.DESCRIPTION]!!,
        url = record[IMAGE.URL]!!,
    )
}

@Repository
open class SqlImageRepository(
    dsl: DSLContext,
    log: Logger = LoggerFactory.forClass()
) :
    SqlCrudRepository<Image, ImageRecord>(IMAGE, IMAGE.ID, dsl, Image::class, log),
    ImageRepository {

    override fun toModel(record: Record) = toImage(record)
}