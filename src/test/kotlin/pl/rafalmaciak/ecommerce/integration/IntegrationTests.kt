package pl.rafalmaciak.ecommerce.integration

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import pl.rafalmaciak.ecommerce.helpers.order
import pl.rafalmaciak.ecommerce.order.Order.CompletedOrder
import pl.rafalmaciak.ecommerce.helpers.expectSuccess
import pl.rafalmaciak.ecommerce.helpers.registerUser


class IntegrationTests : ShouldSpec({

    should("create user and order") {
        // given user is registered
        val user = registerUser {
            firstName = "Alice"
            lastName = "Smith"
            email = "alice.smith@example.com"
            age = 28
        }.expectSuccess()

        // and order exists
        val order = order {
            orderCreator = user
            item {
                quantity = 2
                price = 50.0
            }

            item {
                quantity = 1
                price = 100.0
            }
        }

        // when completing the order
        val completedOrder = order.completeOrder()

        // then the order is completed
        completedOrder.getOrderTotalAmount() shouldBe 200
        completedOrder.shouldBeInstanceOf<CompletedOrder>()
    }
})