package co.digamma.ca.persistence.sql.media

import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.domain.spi.media.ImageRepository
import co.digamma.ca.persistence.jooq.media.tables.records.ImageRecord
import co.digamma.ca.persistence.jooq.media.tables.references.IMAGE
import co.digamma.ca.persistence.sql.SqlCrudRepository
import org.jooq.DSLContext

class SqlImageRepository(dsl: DSLContext):
    SqlCrudRepository<Image, ImageRecord>(IMAGE, IMAGE.ID, dsl, Image::class),
    ImageRepository