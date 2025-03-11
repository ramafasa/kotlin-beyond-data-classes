package pl.rafalmaciak.ecommerce.user

import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistered
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistrationFailure
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistrationFailure.ErrorWhilePersistingUser
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistrationFailure.UserAgeNotValid
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistrationFailure.UserAlreadyExists
import pl.rafalmaciak.ecommerce.user.UserRegistrationResult.UserRegistrationFailure.UserEmailNotValid
import java.util.UUID
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

internal data class User(
    val firstName: String,
    val lastName: String,
    val email: Email,
    val age: Int
) {
    companion object {
        fun create(
            firstName: String,
            lastName: String,
            email: String,
            age: Int
        ): Result<User> {
            if (age !in 18..100) {
                return failure(UserAgeNotValidException())
            }

            val email = runCatching { Email(email) }.getOrElse {
                return failure(it)
            }

            return success(User(firstName, lastName, email, age))
        }
    }
}

internal data class UserDto(
    val firstName: String,
    val lastName: String,
    val email: String,
    val age: Int,
)

internal object UserRegistration {

    fun registerUser(user: UserDto): UserRegistrationResult {
        val user = User.create(
            user.firstName,
            user.lastName,
            user.email,
            user.age
        ).getOrElse {
            return when(it) {
                is UserAgeNotValidException -> UserAgeNotValid
                is InvalidEmailAddressException -> UserEmailNotValid
                else -> UserRegistrationFailure
            }
        }

        // error if user already exists
        if (UserRepository.exists(user)) {
            return UserAlreadyExists
        }

        // user is persisted
        return try {
            UserRepository.persist(user)
            UserRegistered(user)
        } catch (ex: Exception) {
            return return ErrorWhilePersistingUser(ex)
        }
    }
}

internal sealed class UserRegistrationResult {
    data class UserRegistered(val user: User) : UserRegistrationResult()
    object UserRegistrationFailure : UserRegistrationResult() {
        object UserAgeNotValid : UserRegistrationResult()
        object UserEmailNotValid : UserRegistrationResult()
        object UserAlreadyExists : UserRegistrationResult()
        data class ErrorWhilePersistingUser(val error: Throwable) : UserRegistrationResult()
    }
}

internal class UserAgeNotValidException :
    RuntimeException("User age must be between 18 and 100")


@JvmInline
internal value class UserId(val raw: UUID)