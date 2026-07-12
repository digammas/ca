package co.digamma.ca.suites.media

import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.fixtures.givenLocale
import co.digamma.ca.fixtures.givenString
import co.digamma.ca.fixtures.givenUuid

fun givenImage() = Image(
    id = givenUuid(),
    locale = givenLocale(),
    url = "https://example.com/${givenString(12)}.png",
    description = givenString(),
)