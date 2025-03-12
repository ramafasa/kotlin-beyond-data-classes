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
        val user = UserDto("John", "Doe", "john.doe@example.com", 30)
        UserRegistration.registerUser(user).shouldBeInstanceOf<UserRegistered>()
    }

    should("not register user younger than 18 years") {
        val user = UserDto("John", "Doe", "john.doe@example.com", 16)
        UserRegistration.registerUser(user).shouldBeInstanceOf<UserAgeNotValid>()
    }

    should("not register user older than 100 years") {
        val user = UserDto("John", "Doe", "john.doe@example.com", 101)
        UserRegistration.registerUser(user).shouldBeInstanceOf<UserAgeNotValid>()
    }

    should("return false when persistence fails") {
        val user = UserDto("John", "Doe", "john.doe@example.com", 30)

        // Simulate a persistence failure
        UserRepository.shouldFail = true

        UserRegistration.registerUser(user)
            .shouldBeInstanceOf<ErrorWhilePersistingUser>()

        // Reset the flag for subsequent tests
        UserRepository.shouldFail = false
    }
})
