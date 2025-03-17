package pl.rafalmaciak.ecommerce.helpers

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import pl.rafalmaciak.ecommerce.order.Order
import pl.rafalmaciak.ecommerce.order.Order.CompletedOrder
import pl.rafalmaciak.ecommerce.user.User
import pl.rafalmaciak.ecommerce.user.UserDto
import pl.rafalmaciak.ecommerce.user.UserRegistration
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistered

/**
 * DSL entry function for registering a user.
 *
 * Usage:
 *
 * registerUser {
 *     firstName = "Alice"
 *     lastName = "Smith"
 *     email = "alice.smith@example.com"
 *     age = 28O
 * }.expectSuccess()
 *
 * The function returns a UserRegistrationResult. The chained `expectSuccess()`
 * asserts that the registration was successful.
 */
internal fun registerUser(init: UserRegistrationBuilder.() -> Unit): UserRegistrationResult {
    val builder = UserRegistrationBuilder().apply(init)
    val userDto = builder.build()
    return UserRegistration.registerUser(userDto)
}

/**
 * DSL builder for constructing a [UserDto].
 */
internal class UserRegistrationBuilder {
    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var age: Int = 0

    fun build(): UserDto = UserDto(
        firstName = firstName,
        lastName = lastName,
        email = email,
        age = age
    )
}

/**
 * Extension function for asserting that the registration result is successful.
 * If the result is a [UserRegistered], it is returned; otherwise, an AssertionError is thrown.
 */
internal fun UserRegistrationResult.expectSuccess(): User {
    return when (this) {
        is UserRegistered -> this.user
        else -> throw AssertionError("Expected UserRegistered but got $this")
    }
}

internal infix fun Order.shouldBeCompletedWithTotalAmount(expectedTotalAmount: Double) {
    this.shouldBeInstanceOf<CompletedOrder>()
    getOrderTotalAmount() shouldBe expectedTotalAmount

}
