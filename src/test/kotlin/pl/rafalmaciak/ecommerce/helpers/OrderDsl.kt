package pl.rafalmaciak.ecommerce.helpers

import pl.rafalmaciak.ecommerce.order.Order
import pl.rafalmaciak.ecommerce.order.OrderItem
import pl.rafalmaciak.ecommerce.user.User
import kotlin.uuid.Uuid

/**
 * DSL entry function that builds a PendingOrder.
 * Usage of the DSL looks like:
 *
 * val order = order {
 *     userId = Uuid.random()
 *     item {
 *         productId = Uuid.random()
 *         quantity = 2
 *         price = 50.0
 *     }
 *     item {
 *         productId = Uuid.random()
 *         quantity = 1
 *         price = 100.0
 *     }
 * }
 */
internal fun order(init: OrderBuilder.() -> Unit): Order.PendingOrder {
    val builder = OrderBuilder()
    builder.init()
    return builder.build()
}

@EcommerceDsl
internal class OrderBuilder {
    // The user ID is mandatory for creating an order.
    lateinit var orderCreator: User
    // A mutable list to collect order items defined in the DSL.
    private val items = mutableListOf<OrderItem>()

    /**
     * DSL function to add an order item.
     * It accepts a lambda with receiver (OrderItemBuilder) for configuration.
     */
    fun item(init: OrderItemBuilder.() -> Unit) {
        val itemBuilder = OrderItemBuilder()
        itemBuilder.init()
        items.add(itemBuilder.build())
    }

    /**
     * Build the PendingOrder instance using the provided userId and items.
     */
    fun build(): Order.PendingOrder {
        // You could also use the companion object of PendingOrder if desired.
        return Order.PendingOrder(
            orderId = Uuid.random(),
            userId = orderCreator.id.raw,
            items = items
        )
    }
}

@EcommerceDsl
internal class OrderItemBuilder {
    // The productId must be set in the DSL.
    var productId: Uuid = Uuid.random()
    // Default quantity is 1.
    var quantity: Int = 1
    // Default price is 0.0.
    var price: Double = 0.0

    /**
     * Builds an OrderItem instance with the configured values.
     */
    fun build(): OrderItem {
        return OrderItem(
            productId = productId,
            quantity = quantity,
            price = price
        )
    }
}
