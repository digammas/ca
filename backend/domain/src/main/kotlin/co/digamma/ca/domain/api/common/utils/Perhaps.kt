package co.digamma.ca.domain.api.common.utils

/**
 * Potentially empty type container.
 */
sealed interface Perhaps<T> {

    /**
     * Execute a function if a value there is.
     *
     * @param[f] the function to execute.
     * @return a [Present] encapsulating the result of the function, if receiver has a value, or an [Absent] otherwise.
     */
    fun <R> ifPresent(f: (T) -> R): Perhaps<R>

    /**
     * Return self if there is a value.
     * Otherwise, execute a function if the first parameter has a value, otherwise return an [Absent].
     *
     * @param[p] a [Perhaps] to evaluate if the receiver has no value.
     * @param[f] the function to execute.
     * @return the value if present, otherwise result of the function.
     */
    fun <I> elseIfPresent(p: Perhaps<I>, f: (I) -> T): Perhaps<T>

    /**
     * Execute a function if a value there isn't.
     *
     * @param[f] the function to execute.
     * @return the value if present, otherwise result of the function.
     */
    fun orElse(f: () -> T): T

    /**
     * A [Perhaps] implementation where the value is assumed present.
     *
     * *Presence* doesn't imply non-nullability. The value can be present yet null.
     */
    class Present<T>(val value: T): Perhaps<T> {
        override fun <R> ifPresent(f: (T) -> R): Present<R> = present(f(value))
        override fun <I> elseIfPresent(p: Perhaps<I>, f: (I) -> T): Perhaps<T> = this
        override fun orElse(f: () -> T): T = this.value
    }

    /**
     * A [Perhaps] implementation where the value is assumed absent.
     */
    class Absent<T>: Perhaps<T> {
        override fun <R> ifPresent(f: (T) -> R): Absent<R> = absent()
        override fun <I> elseIfPresent(p: Perhaps<I>, f: (I) -> T): Perhaps<T> = p.ifPresent(f)
        override fun orElse(f: () -> T): T = f()
    }

    companion object {
        fun <T> present(value: T): Present<T> = Present(value)
        fun <T> absent(): Absent<T> = Absent()
    }
}

/**
 * Extension method that transform a nullable value to a [Perhaps].
 *
 * If receiver is null, the result will be an [Absent][Perhaps.Absent]
 * Otherwise, the result will be a [Present][Perhaps.Present] of the non-null value.
 */
fun <T> T?.asPerhaps(): Perhaps<T> {
    return if (this == null) Perhaps.absent() else Perhaps.present(this)
}