package co.digamma.ca.persistence.sql.media

import co.digamma.ca.domain.api.common.utils.LoggerFactory
import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.domain.spi.media.ImageRepository
import co.digamma.ca.persistence.jooq.media.tables.records.ImageRecord
import co.digamma.ca.persistence.jooq.media.tables.references.IMAGE
import co.digamma.ca.persistence.sql.SqlCrudRepository
import java.util.logging.Logger
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
open class SqlImageRepository(
    dsl: DSLContext,
    log: Logger = LoggerFactory.forClass()):
    SqlCrudRepository<Image, ImageRecord>(IMAGE, IMAGE.ID, dsl, Image::class, log),
    ImageRepository