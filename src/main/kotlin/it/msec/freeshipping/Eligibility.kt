package it.msec.freeshipping

sealed class EligibilityResult

object Eligible : EligibilityResult()

data class NotEligible(val reasons: List<String>) : EligibilityResult() {
    constructor(reason: String): this(listOf(reason))
}

// ------------------------------------------------

fun isEligible(order: Order): EligibilityResult = TODO("implement your logic here")
