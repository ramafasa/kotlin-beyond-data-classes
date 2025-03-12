package pl.rafalmaciak.ecommerce.user

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe


class UserRegistrationTest : ShouldSpec({

    should("register a user successfully when email is valid and age is within limits") {
        val user = User("John", "Doe", "john.doe@example.com", 30)
        UserRegistration.registerUser(user) shouldBe true
    }

    should("throw InvalidEmailAddressException for an invalid email address") {
        val user = User("John", "Doe", "invalid-email", 30)
        shouldThrow<InvalidEmailAddressException> {
            UserRegistration.registerUser(user)
        }
    }

    should("throw UserMustBeAnAdultException for a user younger than 18") {
        val user = User("John", "Doe", "john.doe@example.com", 16)
        shouldThrow<UserAgeNotValidException> {
            UserRegistration.registerUser(user)
        }
    }

    should("throw UserMustBeAnAdultException for a user older than 100") {
        val user = User("John", "Doe", "john.doe@example.com", 101)
        shouldThrow<UserAgeNotValidException> {
            UserRegistration.registerUser(user)
        }
    }

    should("return false when persistence fails") {
        val user = User("John", "Doe", "john.doe@example.com", 30)

        // Simulate a persistence failure
        UserRepository.shouldFail = true

        UserRegistration.registerUser(user) shouldBe false

        // Reset the flag for subsequent tests
        UserRepository.shouldFail = false
    }
})
