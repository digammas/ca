package co.digamma.ca.fixtures.utils.media

import co.digamma.ca.domain.api.media.Image
import co.digamma.ca.fixtures.utils.givenLocale
import co.digamma.ca.fixtures.utils.givenString
import co.digamma.ca.fixtures.utils.givenUuid

fun givenImage() = Image(
    id = givenUuid(),
    locale = givenLocale(),
    url = "https://example.com/${givenString(12)}.png",
    description = givenString(),
)