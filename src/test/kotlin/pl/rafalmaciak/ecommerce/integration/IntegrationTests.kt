package pl.rafalmaciak.ecommerce.integration

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import pl.rafalmaciak.ecommerce.order.Order
import pl.rafalmaciak.ecommerce.order.Order.CompletedOrder
import pl.rafalmaciak.ecommerce.order.OrderItem
import pl.rafalmaciak.ecommerce.user.UserDto
import pl.rafalmaciak.ecommerce.user.UserRegistration
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistered
import java.util.UUID


class IntegrationTests : ShouldSpec({

    should("create user and order") {
        // given user is registered
        val userDto = UserDto("John", "Doe", "john.doe@example.com", 30)
        UserRegistration.registerUser(userDto)

        val user = when (val user = UserRegistration.registerUser(userDto)) {
            is UserRegistered -> user.user
            else -> throw AssertionError("Expected UserRegistered but got $user")
        }

        // and order exists
        var order = Order.PendingOrder(
            orderId = UUID.randomUUID(),
            userId = user.id.raw,
        )

        order = order.addOrderItem(OrderItem(UUID.randomUUID(), 2, 50.0))
        order = order.addOrderItem(OrderItem(UUID.randomUUID(), 1, 100.0))

        // when completing the order
        val completedOrder = order.completeOrder()

        // then the order is completed
        completedOrder.getOrderTotalAmount() shouldBe 200
        completedOrder.shouldBeInstanceOf<CompletedOrder>()
    }
})