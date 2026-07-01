package co.digamma.ca.fixtures.inmem.cookbook

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.cookbook.QuantifiedIngredient
import co.digamma.ca.domain.spi.cookbook.QuantifiedIngredientRepository
import co.digamma.ca.fixtures.inmem.InMemCrudRepository

@Singleton
class InMemQuantifiedIngredientRepository : InMemCrudRepository<QuantifiedIngredient>(), QuantifiedIngredientRepository

