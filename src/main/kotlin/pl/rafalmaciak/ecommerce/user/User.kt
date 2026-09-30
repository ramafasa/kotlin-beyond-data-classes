package pl.rafalmaciak.ecommerce.user

import kotlin.uuid.Uuid

internal data class User(
    val firstName: String,
    val lastName: String,
    val email: Email,
    val age: Int
)

internal object UserRegistration {

    fun registerUser(user: User): Result<UserId> {
        // user's age must be between 18 and 100
        if (user.age !in 18..100) {
            return Result.failure(UserAgeNotValidException())
        }

        // user is persisted
        try {
            val userId = UserRepository.persist(user)
            return Result.success(UserId(userId))
        } catch (ex: Exception) {
            return Result.failure(ex)
        }
    }
}

@JvmInline
internal value class UserId(val raw: Uuid)

internal class UserAgeNotValidException :
    RuntimeException("User age must be between 18 and 100")