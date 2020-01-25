@file:Suppress("MemberVisibilityCanBePrivate")

package it.msec.freeshipping

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import org.junit.Test

/*

 Free shipping kata
 -----------------------------------

 Given an Order instance, implement the business logic that determines if the provided order is eligible for
 free shipping. In order to be eligible for free shipping, ALL of the following constraints must be satisfied:

  - Shipment is to Italy or Spain

  - Total price should be at least 100 EUR

  - Order contains at least 2 distinct product codes or a total of 5 items (even with the same code)

 If not eligible, provide the reasons of the negative result.

*/

class EligibilityTest {

    val aProduct = Product("aProductCode", "A very nice product")
    val anotherProduct = Product("anotherProductCode", "A not so good product")

    val eligibleOrder = Order(
            number = 3213,
            totalPrice = 200,
            shipmentAddress = Address("Piazza Duomo, 1", "Milan", Italy),
            orderLines = listOf(
                    OrderLine(aProduct, 3),
                    OrderLine(anotherProduct, 6)
            ))

    @Test
    fun `order eligible`() {
        val eligibilityResult = isEligible(eligibleOrder)
        assertThat(eligibilityResult).isEqualTo(Eligible)
    }

    @Test
    fun `order not eligible due to shipment country`() {

        val order = eligibleOrder.copy(
                shipmentAddress = Address("Tour Eiffel", "Paris", France)
        )

        val eligibilityResult = isEligible(order)

        assertNotEligibleForReasons("Bad country")(eligibilityResult)
    }

    @Test
    fun `order not eligible due to low total price`() {

        val order = eligibleOrder.copy(
                totalPrice = 30
        )

        val eligibilityResult = isEligible(order)

        assertNotEligibleForReasons("Bad total price")(eligibilityResult)
    }

    @Test
    fun `order not eligible due to both low total price and shipment country`() {

        val order = eligibleOrder.copy(
                totalPrice = 30,
                shipmentAddress = Address("Tour Eiffel", "Paris", France)
        )

        val eligibilityResult = isEligible(order)

        assertNotEligibleForReasons(
                "Bad country",
                "Bad total price")(eligibilityResult)
    }

    @Test
    fun `order not eligible due product constraints not satisfied`() {

        val order = eligibleOrder.copy(
                orderLines = listOf(OrderLine(aProduct, 1))
        )

        val eligibilityResult = isEligible(order)

        assertNotEligibleForReasons(
                "Bad distinct products",
                "Bad total products")(eligibilityResult)
    }

    @Test
    fun `order not eligible due to ALL constraints not satisfied`() {

        val order = eligibleOrder.copy(
                totalPrice = 30,
                shipmentAddress = Address("Tour Eiffel", "Paris", France),
                orderLines = listOf(OrderLine(aProduct, 1))
        )

        val eligibilityResult = isEligible(order)

        assertNotEligibleForReasons(
                "Bad country",
                "Bad total price",
                "Bad distinct products",
                "Bad total products")(eligibilityResult)
    }

    fun assertNotEligibleForReasons(vararg reasons: String) = { result: EligibilityResult<String> ->
        assertThat(result).isInstanceOf(NotEligible::class)
                .transform { it.reasons }
                .containsOnly(*reasons)
    }
}
