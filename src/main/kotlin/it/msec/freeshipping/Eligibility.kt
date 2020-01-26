@file:Suppress("ObjectPropertyName", "DuplicatedCode")

package it.msec.freeshipping

val extractShipmentCountry = { order: Order -> order.shipmentAddress.country }

val extractTotalPrice = { order: Order -> order.totalPrice }

val extractItemsCount = { order: Order -> order.orderLines.map { it.amount }.sum() }

val extractDistinctProductsCount = { order: Order -> order.orderLines.map { it.product.code }.distinct().size }

val `shipping country is IT or ES` =
        extractShipmentCountry andThen checkIfValueIsIn(Italy, Spain, failureReason = "Bad country")

val `total price is at least 100 EUR` =
        extractTotalPrice andThen checkIfValueIsAtLeast(100, failureReason = "Bad total price")

val `contains at least 5 items` =
        extractItemsCount andThen checkIfValueIsAtLeast(5, failureReason = "Bad items count")

val `contains at least 2 distinct products` =
        extractDistinctProductsCount andThen checkIfValueIsAtLeast(2, failureReason = "Bad distinct products")

val isEligible = (
        `shipping country is IT or ES`
                and `total price is at least 100 EUR`
                and (`contains at least 5 items` or `contains at least 2 distinct products`)
        )























