## Free shipping kata

Given an Order instance, implement the business logic that determines if the provided order is eligible for
free shipping. In order to be eligible for free shipping, ALL of the following constraints must be satisfied:

  - Shipment is to Italy or Spain

  - Total price should be at least 100 EUR

  - Order contains at least 2 distinct product codes or a total of 5 items (even with the same code)

If not eligible, provide the reasons of the negative result.

