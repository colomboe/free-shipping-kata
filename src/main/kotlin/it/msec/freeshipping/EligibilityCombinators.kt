package it.msec.freeshipping

sealed class EligibilityResult<out R>

object Eligible : EligibilityResult<Nothing>()

data class NotEligible<R>(val reasons: List<R>) : EligibilityResult<R>() {
    constructor(reason: R) : this(listOf(reason))
}

fun <T, R> checkValue(failureReason: R, f: (T) -> Boolean): (T) -> EligibilityResult<R> = { value ->
    if (f(value)) Eligible else NotEligible(failureReason)
}

fun <T, R> checkIfValueIsIn(vararg eligibleValues: T, failureReason: R): (T) -> EligibilityResult<R> =
        checkValue(failureReason) { it in eligibleValues }

fun <R> checkIfValueIsAtLeast(minValue: Int, failureReason: R): (Int) -> EligibilityResult<R> =
        checkValue(failureReason) { it >= minValue }

infix fun <T, R> ((T) -> EligibilityResult<R>).and(that: (T) -> EligibilityResult<R>): (T) -> EligibilityResult<R> = { t ->
    val r1 = this(t)
    val r2 = that(t)
    when {
        r1 is NotEligible && r2 is NotEligible -> NotEligible(r1.reasons + r2.reasons)
        r1 is NotEligible && r2 is Eligible -> r1
        r1 is Eligible && r2 is NotEligible -> r2
        else -> Eligible
    }
}

infix fun <T, R> ((T) -> EligibilityResult<R>).or(that: (T) -> EligibilityResult<R>): (T) -> EligibilityResult<R> = { t ->
    val r1 = this(t)
    val r2 = that(t)
    when {
        r1 is NotEligible && r2 is NotEligible -> NotEligible(r1.reasons + r2.reasons)
        r1 is NotEligible && r2 is Eligible -> r2
        r1 is Eligible && r2 is NotEligible -> r1
        else -> Eligible
    }
}
