package pl.rafalmaciak.ecommerce.user

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistered
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistrationFailure.ErrorWhilePersistingUser
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistrationFailure.UserAgeNotValid
import kotlin.uuid.Uuid


class UserRegistrationTest : ShouldSpec({

    should("register a user successfully when email is valid and age is within limits") {
        val user = UserDto("John", "Doe", "john.doe@example.com", 30)

        with(InMemoryUserRepository()) {
            UserRegistration.registerUser(user).shouldBeInstanceOf<UserRegistered>()
        }
    }

    should("not register user younger than 18 years") {
        val user = UserDto("John", "Doe", "john.doe@example.com", 16)

        with(InMemoryUserRepository()) {
            UserRegistration.registerUser(user).shouldBeInstanceOf<UserAgeNotValid>()
        }
    }

    should("not register user older than 100 years") {
        val user = UserDto("John", "Doe", "john.doe@example.com", 101)

        with(InMemoryUserRepository()) {
            UserRegistration.registerUser(user).shouldBeInstanceOf<UserAgeNotValid>()
        }
    }

    should("not register user when persistence fails") {
        val user = UserDto("John", "Doe", "john.doe@example.com", 30)
        val failingRepository = object : UserRepository by InMemoryUserRepository() {
            override fun persist(user: User): Uuid = throw RuntimeException("Failed to persist user")
        }

        with(failingRepository) {
            UserRegistration.registerUser(user).shouldBeInstanceOf<ErrorWhilePersistingUser>()
        }
    }
})
