package co.digamma.ca.persistence.sql

import co.digamma.ca.domain.api.model.Model
import co.digamma.ca.domain.spi.CrudRepository

interface SqlCrudRepository<T: Model>: CrudRepository<T> {
}