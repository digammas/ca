package co.digamma.ca.suites.users

import co.digamma.ca.domain.api.users.User
import co.digamma.ca.fixtures.givenString

fun givenUser() = User(
    username = givenString(12),
    password = givenString(24),
    email = "${givenString(8)}@example.com",
)