package pl.rafalmaciak.ecommerce.user

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistered
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistrationFailure.ErrorWhilePersistingUser
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistrationFailure.UserAgeNotValid
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistrationFailure.UserAlreadyExists


class UserRegistrationTest : ShouldSpec({

    beforeTest {
        UserRepository.clear()
    }

    should("register a user successfully when email is valid and age is within limits") {
        val user = User("John", "Doe", Email("john.doe@example.com"), 30)
        UserRegistration.registerUser(user).shouldBeInstanceOf<UserRegistered>()
    }

    should("not register user younger than 18 years") {
        val user = User("John", "Doe", Email("john.doe@example.com"), 16)
        UserRegistration.registerUser(user).shouldBeInstanceOf<UserAgeNotValid>()
    }

    should("not register user older than 100 years") {
        val user = User("John", "Doe", Email("john.doe@example.com"), 101)
        UserRegistration.registerUser(user).shouldBeInstanceOf<UserAgeNotValid>()
    }

    should("not register user if already exists") {
        val user = User("John", "Doe", Email("john.doe@example.com"), 20)
        UserRegistration.registerUser(user).shouldBeInstanceOf<UserRegistered>()
        UserRegistration.registerUser(user).shouldBeInstanceOf<UserAlreadyExists>()
    }

    should("return false when persistence fails") {
        val user = User("John", "Doe", Email("john.doe@example.com"), 30)

        // Simulate a persistence failure
        UserRepository.shouldFail = true

        UserRegistration.registerUser(user)
            .shouldBeInstanceOf<ErrorWhilePersistingUser>()

        // Reset the flag for subsequent tests
        UserRepository.shouldFail = false
    }
})
