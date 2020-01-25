@file:Suppress("ObjectPropertyName", "DuplicatedCode")

package it.msec.freeshipping

sealed class EligibilityResult<out R>

object Eligible : EligibilityResult<Nothing>()

data class NotEligible<R>(val reasons: List<R>) : EligibilityResult<R>() {
    constructor(reason: R): this(listOf(reason))
}

// ------------------------------------------------

fun <T, R> checkValue(reason: R, f: (T) -> Boolean): (T) -> EligibilityResult<R> = { value ->
    if (f(value)) Eligible else NotEligible(reason)
}

fun <T, R> checkIfValueIsIn(eligibleValues: List<T>, reason: R): (T) -> EligibilityResult<R> =
        checkValue(reason) { it in eligibleValues }

fun <R> checkIfValueIsAtLeast(minValue: Int, reason: R): (Int) -> EligibilityResult<R> =
        checkValue(reason) { it >= minValue }

infix fun <T, R> ((T) -> EligibilityResult<R>).and(that: (T) -> EligibilityResult<R>): (T) -> EligibilityResult<R> = { t ->
    val fr = this(t)
    val gr = that(t)
    when {
        fr is NotEligible && gr is NotEligible -> NotEligible(fr.reasons + gr.reasons)
        fr is NotEligible && gr is Eligible -> fr
        fr is Eligible && gr is NotEligible -> gr
        else -> Eligible
    }
}

infix fun <T, R> ((T) -> EligibilityResult<R>).or(that: (T) -> EligibilityResult<R>): (T) -> EligibilityResult<R> = { t ->
    val fr = this(t)
    val gr = that(t)
    when {
        fr is NotEligible && gr is NotEligible -> NotEligible(fr.reasons + gr.reasons)
        fr is NotEligible && gr is Eligible -> gr
        fr is Eligible && gr is NotEligible -> fr
        else -> Eligible
    }
}




val extractShipmentCountry = { order: Order -> order.shipmentAddress.country }

val extractTotalPrice = { order: Order -> order.totalPrice }

val extractProductsCount = { order: Order -> order.orderLines.map { it.amount }.reduce { a, b -> a + b } }

val extractDistinctProductsCount = { order: Order -> order.orderLines.map { it.product.code }.distinct().size }

val `shipping country is IT or ES` =
        extractShipmentCountry andThen checkIfValueIsIn(listOf(Italy, Spain), "Bad country")

val `total price is at least 100 EUR` =
        extractTotalPrice andThen checkIfValueIsAtLeast(100, "Bad total price")

val `contains at least 5 products` =
        extractProductsCount andThen checkIfValueIsAtLeast(5, "Bad total products")

val `contains at least 2 distinct products` =
        extractDistinctProductsCount andThen checkIfValueIsAtLeast(2, "Bad distinct products")

val isEligible = `shipping country is IT or ES` and `total price is at least 100 EUR` and
        (`contains at least 5 products` or `contains at least 2 distinct products`)























