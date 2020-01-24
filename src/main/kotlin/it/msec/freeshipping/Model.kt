package it.msec.freeshipping

typealias ProductCode = String
typealias OrderNumber = Int
typealias Money = Int

sealed class AvailableCountry
object Italy : AvailableCountry()
object Spain : AvailableCountry()
object France : AvailableCountry()
object Germany : AvailableCountry()
object Greece : AvailableCountry()

data class Product(val code: ProductCode,
                   val description: String)

data class OrderLine(val product: Product,
                     val amount: Int)

data class Address(val road: String,
                   val city: String,
                   val country: AvailableCountry)

data class Order(val number: OrderNumber,
                 val totalPrice: Money,
                 val shipmentAddress: Address,
                 val orderLines: List<OrderLine>)

