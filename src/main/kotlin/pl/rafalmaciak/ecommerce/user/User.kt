package pl.rafalmaciak.ecommerce.user

import pl.rmaciak.ecommerce.user.Email
import java.util.UUID

internal data class User(
    val firstName: String,
    val lastName: String,
    val email: Email,
    val age: Int
)

internal object UserRegistration {

    fun registerUser(user: User): Result<UserId> {
        // user must be 18 years or older
        if (user.age !in 18..100) {
            throw UserMustBeAnAdultException()
        }

        // user is persisted
        try {
            val userId = UserRepository.persist(user)
            return Result.success(UserId(userId))
        } catch (ex: Exception) {
            return return Result.failure(ex)
        }
    }
}

@JvmInline
internal value class UserId(val raw: UUID)

internal class UserMustBeAnAdultException :
    RuntimeException("User must be 18 years or older to register account")