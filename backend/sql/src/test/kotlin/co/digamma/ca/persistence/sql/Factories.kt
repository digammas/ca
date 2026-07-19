package co.digamma.ca.persistence.sql

import org.springframework.beans.factory.ObjectFactory
import java.time.Instant

val instantFactory = ObjectFactory { Instant.now() }