package co.digamma.ca.suites.cookbook

import co.digamma.ca.domain.api.cookbook.Ingredient
import co.digamma.ca.domain.api.cookbook.MeasurementUnit
import co.digamma.ca.domain.api.cookbook.Recipe
import co.digamma.ca.domain.api.cookbook.Step
import co.digamma.ca.suites.food.givenDish
import co.digamma.ca.fixtures.givenLocale
import co.digamma.ca.fixtures.givenString
import co.digamma.ca.fixtures.givenUuid
import co.digamma.ca.suites.users.givenUser
import java.time.Instant

fun givenIngredient() = Ingredient(
    id = givenUuid(),
    locale = givenLocale(),
    name = givenString(),
    description = givenString(),
)

fun givenMeasurementUnit() = MeasurementUnit(
    id = givenUuid(),
    locale = givenLocale(),
    name = givenString(),
    dimension = MeasurementUnit.Dimension.entries.random(),
)

fun giveRecipe() = Recipe(
    id = givenUuid(),
    locale = givenLocale(),
    dish = givenDish(),
    yield = 4,
    createdAt = Instant.now(),
    updatedAt = Instant.now(),
    author = givenUser(),
    timeToServe = 30,
)

fun givenStep() = Step(
    id = givenUuid(),
    locale = givenLocale(),
    description = givenString(42),
    estimatedTime = 10,
    recipe = giveRecipe(),
)