package co.digamma.ca.persistence.sql

import org.jooq.Converter
import java.util.Locale

class LocaleConverter : Converter<String?, Locale?> by Converter.ofNullable(
    String::class.java,
    Locale::class.java,
    Locale::forLanguageTag,
    Locale::toLanguageTag,
) {
    override fun fromSupported() = true
    override fun toSupported() = true
}