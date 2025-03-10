package pl.rafalmaciak.ecommerce.user

internal data class User(
    val firstName: String,
    val lastName: String,
    val email: Email,
    val age: Int
)

internal object UserRegistration {

    fun registerUser(user: User): Boolean {
        // user's age must be between 18 and 100
        if (user.age !in 18..100) {
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
