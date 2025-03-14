package pl.rafalmaciak.ecommerce.user

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe


class UserRegistrationTest : ShouldSpec({

    should("register a user successfully when email is valid and age is within limits") {
        val user = User("John", "Doe", Email("john.doe@example.com"), 30)
        UserRegistration.registerUser(user).isSuccess shouldBe true
    }

    should("not register user younger than 18 years") {
        val user = User("John", "Doe", Email("john.doe@example.com"), 16)
        UserRegistration.registerUser(user).isFailure shouldBe true
    }

    should("not register user older than 100") {
        val user = User("John", "Doe", Email("john.doe@example.com"), 101)
        UserRegistration.registerUser(user).isFailure shouldBe true
    }

    should("return false when persistence fails") {
        val user = User("John", "Doe", Email("john.doe@example.com"), 30)

        // Simulate a persistence failure
        UserRepository.shouldFail = true

        UserRegistration.registerUser(user).isFailure shouldBe true

        // Reset the flag for subsequent tests
        UserRepository.shouldFail = false
    }
})
