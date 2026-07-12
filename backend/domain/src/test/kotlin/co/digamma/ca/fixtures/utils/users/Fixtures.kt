package co.digamma.ca.fixtures.utils.users

import co.digamma.ca.domain.api.users.User
import co.digamma.ca.fixtures.utils.givenString

fun givenUser() = User(
    username = givenString(12),
    password = givenString(24),
    email = "${givenString(8)}@example.com",
)