package pl.rafalmaciak.ecommerce.order

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.doubles.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import pl.rafalmaciak.ecommerce.order.OrderStatus.CANCELLED
import pl.rafalmaciak.ecommerce.order.OrderStatus.COMPLETED
import pl.rafalmaciak.ecommerce.order.OrderStatus.PENDING
import pl.rafalmaciak.ecommerce.order.OrderStatus.SHIPPED
import java.util.UUID

class OrderTests : ShouldSpec({

    fun createOrder(
        status: OrderStatus,
        items: List<OrderItem> = emptyList(),
        totalAmount: Double? = null,
        shippingAddress: String? = null
    ): Order {
        return Order(
            orderId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            status = status,
            items = items,
            totalAmount = totalAmount,
            shippingAddress = shippingAddress
        )
    }

    context("cancelling order") {
        should("cancel an order with PENDING status") {
            val order = createOrder(PENDING)
            val cancelledOrder = order.cancelOrder()
            cancelledOrder.status shouldBe CANCELLED
        }

        should("cancel an order with COMPLETED status") {
            val order = createOrder(COMPLETED)
            val cancelledOrder = order.cancelOrder()
            cancelledOrder.status shouldBe CANCELLED
        }

        should("fail to cancel an order with SHIPPED status") {
            val order = createOrder(SHIPPED)
            val exception = shouldThrow<IllegalStateException> { order.cancelOrder() }
            exception.message shouldContain "Order in status SHIPPED cannot be cancelled"
        }

        should("fail to cancel an order with CANCELLED status") {
            val order = createOrder(CANCELLED)
            val exception = shouldThrow<IllegalStateException> { order.cancelOrder() }
            exception.message shouldContain "Order in status CANCELLED cannot be cancelled"
        }
    }

    context("adding item to order") {
        val orderItem = OrderItem(
            productId = UUID.randomUUID(),
            quantity = 2,
            price = 50.0
        )

        should("add an order item to an order with PENDING status") {
            val order = createOrder(PENDING, items = emptyList(), totalAmount = 0.0)
            val updatedOrder = order.addOrderItem(orderItem)

            updatedOrder.getOrderTotalAmount().shouldBeExactly(50.0 * 2)
        }

        listOf(COMPLETED, SHIPPED, CANCELLED).forEach { status ->
            should("fail to add an order item to an order with $status status") {
                val order = createOrder(status)
                val exception = shouldThrow<IllegalStateException> { order.addOrderItem(orderItem) }
                exception.message shouldContain "Order in status $status cannot be modified"
            }
        }
    }

    context("retrieving total amount of order") {
        should("return the provided totalAmount when not null") {
            val order = createOrder(COMPLETED, totalAmount = 100.0)
            order.getOrderTotalAmount() shouldBe 100.0
        }

        should("return 0.0 when totalAmount is null") {
            val order = createOrder(COMPLETED, totalAmount = null)
            order.getOrderTotalAmount() shouldBe 0.0
        }
    }

    context("completing order") {
        should("complete an order with PENDING status and non-empty items") {
            val orderItem = OrderItem(
                productId = UUID.randomUUID(),
                quantity = 1,
                price = 100.0
            )

            val order = createOrder(PENDING, items = listOf(orderItem), totalAmount = 100.0)
            val completedOrder = order.completeOrder()

            completedOrder.status shouldBe COMPLETED
        }

        listOf(COMPLETED, SHIPPED, CANCELLED).forEach { status ->
            should("fail to complete an order with $status status") {
                val order = createOrder(
                    status,
                    items = listOf(OrderItem(UUID.randomUUID(), 1, 50.0)),
                    totalAmount = 50.0
                )
                val exception = shouldThrow<IllegalStateException> { order.completeOrder() }
                exception.message shouldContain "Order in status $status cannot be completed"
            }
        }

        should("fail to complete an order with empty items") {
            val order = createOrder(PENDING, items = emptyList(), totalAmount = 0.0)
            val exception = shouldThrow<IllegalStateException> { order.completeOrder() }
            exception.message shouldContain "Order items must not be empty"
        }
    }

    context("shipping order and getting shipping address") {
        should("ship an order and set the shipping address") {
            val order = createOrder(COMPLETED)
            val shippedOrder = order.shipOrder("123 Main St")
            shippedOrder.getShippingAddress() shouldBe "123 Main St"
            shippedOrder.status shouldBe SHIPPED
        }

        should("fail to get shipping address when not set") {
            val nonShippedOrder = createOrder(SHIPPED)
            val exception =
                shouldThrow<IllegalStateException> { nonShippedOrder.getShippingAddress() }
            exception.message shouldContain "Shipping address is not set"
        }

        listOf(PENDING, SHIPPED, CANCELLED).forEach { status ->
            should("fail to ship an order that is in status $status") {
                val order = createOrder(status)
                val exception =
                    shouldThrow<IllegalStateException> { order.shipOrder("123 Main St") }
                exception.message shouldContain "Order in status $status cannot be shipped"
            }
        }
    }
})
