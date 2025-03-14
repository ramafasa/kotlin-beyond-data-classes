package pl.rafalmaciak.ecommerce.order

import pl.rafalmaciak.ecommerce.order.OrderStatus.CANCELLED
import pl.rafalmaciak.ecommerce.order.OrderStatus.COMPLETED
import pl.rafalmaciak.ecommerce.order.OrderStatus.PENDING
import pl.rafalmaciak.ecommerce.order.OrderStatus.SHIPPED
import java.util.UUID

internal data class Order(
    val orderId: UUID,
    val userId: UUID,
    val status: OrderStatus,
    val items: List<OrderItem>,
    val totalAmount: Double?,
    private val shippingAddress: String?,
) {

    fun cancelOrder(): Order =
        check(status in listOf(PENDING, COMPLETED)) {
            "Order in status $status cannot be cancelled"
        }.let {
            this.copy(status = CANCELLED)
        }

    fun addOrderItem(orderItem: OrderItem): Order {
        check(status == PENDING) {
            "Order in status $status cannot be modified"
        }

        return this.copy(
            items = this.items + orderItem,
        )
    }

    fun getOrderTotalAmount(): Double =
        totalAmount ?: 0.0

    fun completeOrder(): Order {
        check(status == PENDING) { "Order in status $status cannot be completed" }
        check(items.isNotEmpty()) { "Order items must not be empty" }
        return this.copy(
            status = COMPLETED,
            totalAmount = items.sumOf { it.price * it.quantity },
        )
    }

    fun shipOrder(shippingAddress: String): Order =
        check(status == COMPLETED) {
            "Order in status $status cannot be shipped"
        }.let {
            this.copy(
                status = SHIPPED,
                shippingAddress = shippingAddress,
            )
        }

    fun getShippingAddress(): String {
        check(status == SHIPPED) { "Order in status $status does not have a shipping address" }
        check(shippingAddress != null) { "Shipping address is not set" }
        return requireNotNull(shippingAddress)
    }

    companion object {
        fun createPendingOrder(
            userId: UUID,
        ) {
            Order(
                orderId = UUID.randomUUID(),
                userId = userId,
                status = PENDING,
                items = emptyList(),
                totalAmount = null,
                shippingAddress = null,
            )
        }
    }
}

internal enum class OrderStatus {
    PENDING, // Order has been created and items can be added
    COMPLETED, // order completed and total amount calculated, items cannot be added
    SHIPPED, // order has been shipped to the buyer
    CANCELLED, // order has been cancelled by the buyer
}

internal data class OrderItem(
    val productId: UUID,
    val quantity: Int,
    val price: Double,
)