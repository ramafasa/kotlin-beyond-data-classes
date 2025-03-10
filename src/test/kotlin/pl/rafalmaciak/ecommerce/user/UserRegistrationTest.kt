package pl.rafalmaciak.ecommerce.user

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import pl.rmaciak.ecommerce.user.Email


class UserRegistrationTest : ShouldSpec({

    should("register a user successfully when email is valid and age is within limits") {
        val user = User("John", "Doe", Email("john.doe@example.com"), 30)
        UserRegistration.registerUser(user) shouldBe true
    }

    should("throw UserMustBeAnAdultException for a user younger than 18") {
        val user = User("John", "Doe", Email("john.doe@example.com"), 16)
        shouldThrow<UserAgeNotValidException> {
            UserRegistration.registerUser(user)
        }
    }

    should("throw UserMustBeAnAdultException for a user older than 100") {
        val user = User("John", "Doe", Email("john.doe@example.com"), 101)
        shouldThrow<UserAgeNotValidException> {
            UserRegistration.registerUser(user)
        }
    }

    should("return false when persistence fails") {
        val user = User("John", "Doe", Email("john.doe@example.com"), 30)

        // Simulate a persistence failure
        UserRepository.shouldFail = true

        UserRegistration.registerUser(user) shouldBe false

        // Reset the flag for subsequent tests
        UserRepository.shouldFail = false
    }
})
