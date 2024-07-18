package co.digamma.ca.fixtures.utils

import java.util.Base64
import java.util.Random
import kotlin.math.ceil

val random = Random()

/**
 * Random feed generator.
 */
object RandGen {

    /**
     * Generate random string with given length.
     *
     * The length request is always respected. However, only length that are multiple of 4 produce a string that is
     * fully random. Other length would have one or two trailing equal marks.
     *
     * @param[length] requested string length
     * @return a pseudo random string of the given length
     */
    fun string(length: Int = 16): String {
        val bytesLength: Int = ceil(length * 3.0 / 4).toInt()
        val bytes = ByteArray(bytesLength)
        random.nextBytes(bytes)
        return Base64.getUrlEncoder().encodeToString(bytes)
    }
}