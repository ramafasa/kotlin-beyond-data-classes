package pl.rafalmaciak.ecommerce.user

import pl.rmaciak.ecommerce.user.Email

internal data class User(
    val firstName: String,
    val lastName: String,
    val email: Email,
    val age: Int
)

internal object UserRegistration {

    fun registerUser(user: User): Boolean {
        // user's age must be between 18 and 100
        if (user.age < 18 || user.age > 100) {
            throw UserAgeNotValidException()
        }

        // user is persisted
        try {
            UserRepository.persist(user)
            return true
        } catch (_: Exception) {
            return false
        }
    }
}

internal class UserAgeNotValidException :
    RuntimeException("User age must be between 18 and 100")
