package pl.rafalmaciak.ecommerce.user

internal data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val age: Int
)

internal object UserRegistration {

    fun registerUser(user: User): Boolean {
        // provided e-mail address must be valid
        if (!isValidEmailAddress(user.email)) {
            throw InvalidEmailAddressException(user.email)
        }

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

    private fun isValidEmailAddress(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return email.matches(emailRegex)
    }
}


internal class UserAgeNotValidException :
    RuntimeException("User age must be between 18 and 100")

internal class InvalidEmailAddressException(email: String) :
    RuntimeException("Invalid email address $email")

