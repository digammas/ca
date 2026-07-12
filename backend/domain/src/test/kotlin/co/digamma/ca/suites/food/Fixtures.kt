package co.digamma.ca.suites.food

import co.digamma.ca.domain.api.cookbook.QuantifiedIngredient
import co.digamma.ca.domain.api.food.Course
import co.digamma.ca.domain.api.food.Cuisine
import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.api.food.Serving
import co.digamma.ca.suites.cookbook.giveRecipe
import co.digamma.ca.suites.cookbook.givenIngredient
import co.digamma.ca.suites.cookbook.givenMeasurementUnit
import co.digamma.ca.fixtures.givenLocale
import co.digamma.ca.fixtures.givenString
import co.digamma.ca.fixtures.givenUuid

fun givenCourse() = Course(
    id = givenUuid(),
    locale = givenLocale(),
    name = givenString(),
)

fun givenCuisine() = Cuisine(
    id = givenUuid(),
    locale = givenLocale(),
    name = givenString(),
)

fun givenServing() = Serving(
    id = givenUuid(),
    locale = givenLocale(),
    name = givenString(),
)

fun givenDish() = Dish(
    id = givenUuid(),
    locale = givenLocale(),
    name = givenString(),
    course = givenCourse(),
    cuisine = givenCuisine(),
    serving = givenServing(),
)

fun givenQuantifiedIngredient() = QuantifiedIngredient(
    id = givenUuid(),
    ingredient = givenIngredient(),
    quantity = 3.5f,
    unit = givenMeasurementUnit(),
    recipe = giveRecipe(),
)