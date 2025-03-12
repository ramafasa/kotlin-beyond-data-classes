package pl.rafalmaciak.ecommerce.order

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import pl.rafalmaciak.ecommerce.order.Order.CancelledOrder
import java.util.UUID

class OrderTests : ShouldSpec({

    context("cancelling order") {
        should("cancel a pending order") {
            val order = Order.PendingOrder(
                orderId = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                items = emptyList(),
            )

            val cancelledOrder = order.cancelOrder()

            cancelledOrder.shouldBeInstanceOf<CancelledOrder>()
        }

        should("cancel a completed order") {
            val order = Order.CompletedOrder(
                orderId = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                items = emptyList(),
                totalAmount = 0.0
            )

            val cancelledOrder = order.cancelOrder()

            cancelledOrder.shouldBeInstanceOf<CancelledOrder>()
        }
    }

    context("completing order") {
        should("complete a pending order with non-empty items") {
            val orderItem = OrderItem(
                productId = UUID.randomUUID(),
                quantity = 1,
                price = 100.0
            )

            val order = Order.PendingOrder(
                orderId = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                items = emptyList(),
            )

            val updatedOrder = order.addOrderItem(orderItem)
            val completedOrder = updatedOrder.completeOrder()

            completedOrder.getOrderTotalAmount() shouldBe 100
            completedOrder.shouldBeInstanceOf<Order.CompletedOrder>()
        }

        should("fail to complete an order with empty items") {
            val order = Order.PendingOrder(
                orderId = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                items = emptyList(),
            )

            val exception = shouldThrow<IllegalStateException> { order.completeOrder() }

            exception.message shouldContain "Order items must not be empty"
        }
    }

    context("shipping order and getting shipping address") {
        should("ship an order and set the shipping address") {
            val orderItem = OrderItem(
                productId = UUID.randomUUID(),
                quantity = 1,
                price = 100.0
            )

            val order = Order.CompletedOrder(
                orderId = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                items = listOf(orderItem),
                totalAmount = 100.0
            )

            val shippedOrder = order.shipOrder("123 Main St")
            shippedOrder.shippingAddress shouldBe "123 Main St"
            shippedOrder.shouldBeInstanceOf<Order.ShippedOrder>()
        }
    }
})
