package pl.rafalmaciak.ecommerce.order

import java.util.UUID


internal sealed class Order(
    open val orderId: UUID,
    open val userId: UUID,
    open val items: List<OrderItem>
) {

    interface CancellableOrder {
        fun cancelOrder(): CancelledOrder
    }

    internal data class PendingOrder(
        override val orderId: UUID = UUID.randomUUID(),
        override val userId: UUID,
        override val items: List<OrderItem> = emptyList(),
    ) : Order(orderId, userId, items), CancellableOrder {

        fun addOrderItem(orderItem: OrderItem): PendingOrder =
            this.copy(
                items = this.items + orderItem,
            )

        fun completeOrder(): CompletedOrder {
            check(items.isNotEmpty()) { "Order items must not be empty" }
            return CompletedOrder(orderId, userId, items, items.sumOf { it.price * it.quantity })
        }

        override fun cancelOrder(): CancelledOrder =
            CancelledOrder(orderId, userId, items)

        companion object {
            fun createPendingOrder(
                userId: UUID,
            ) {
                PendingOrder(
                    orderId = UUID.randomUUID(),
                    userId = userId,
                    items = emptyList(),
                )
            }
        }
    }

    internal data class CompletedOrder(
        override val orderId: UUID,
        override val userId: UUID,
        override val items: List<OrderItem>,
        val totalAmount: Double,
    ) : Order(orderId, userId, items), CancellableOrder {

        fun getOrderTotalAmount(): Double = totalAmount

        override fun cancelOrder(): CancelledOrder =
            CancelledOrder(orderId, userId, items)

        fun shipOrder(shippingAddress: String): ShippedOrder =
            ShippedOrder(
                orderId = orderId,
                userId = userId,
                items = items,
                totalAmount = totalAmount,
                shippingAddress = shippingAddress,
            )
    }

    internal data class ShippedOrder(
        override val orderId: UUID,
        override val userId: UUID,
        override val items: List<OrderItem>,
        val totalAmount: Double,
        val shippingAddress: String,
    ) : Order(orderId, userId, items)

    internal data class CancelledOrder(
        override val orderId: UUID,
        override val userId: UUID,
        override val items: List<OrderItem>,
    ) : Order(orderId, userId, items)
}

internal data class OrderItem(
    val productId: UUID,
    val quantity: Int,
    val price: Double,
)