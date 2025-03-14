package pl.rafalmaciak.ecommerce.user

import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistered
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistrationFailure.ErrorWhilePersistingUser
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistrationFailure.UserAgeNotValid
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistrationFailure.UserAlreadyExists
import java.util.UUID

internal data class User(
    val firstName: String,
    val lastName: String,
    val email: Email,
    val age: Int
)

internal object UserRegistration {

    fun registerUser(user: User): UserRegistrationResult {
        // error if user already exists
        if (UserRepository.exists(user)) {
            return UserAlreadyExists
        }

        // user's age must be between 18 and 100
        if (user.age !in 18..100) {
            return UserAgeNotValid
        }

        // user is persisted
        return try {
            val userId = UserRepository.persist(user)
            UserRegistered(UserId(userId))
        } catch (ex: Exception) {
            ErrorWhilePersistingUser(ex)
        }
    }
}

internal sealed class UserRegistrationResult {
    data class UserRegistered(val userId: UserId) : UserRegistrationResult()
    object UserRegistrationFailure : UserRegistrationResult() {
        object UserAgeNotValid : UserRegistrationResult()
        object UserAlreadyExists : UserRegistrationResult()
        data class ErrorWhilePersistingUser(val error: Throwable) : UserRegistrationResult()
    }
}

@JvmInline
internal value class UserId(val raw: UUID)