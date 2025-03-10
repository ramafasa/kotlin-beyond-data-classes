package pl.rafalmaciak.ecommerce.user

internal data class User(
    val firstName: String,
    val lastName: String,
    val email: Email,
    val age: Int
)

internal object UserRegistration {

    fun registerUser(user: User): Boolean {
        // user must be 18 years or older
        if (user.age < 18) {
            throw UserMustBeAnAdultException()
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

internal class UserMustBeAnAdultException :
    RuntimeException("User must be 18 years or older to register account")
