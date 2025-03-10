package pl.rmaciak.ecommerce.user

internal data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val age: Int
)

internal class UserRegistration {

    fun registerUser(user: User): Boolean {
        // user must be 18 years or older
        if (user.age < 18) {
            throw UserMustBeAnAdultException()
        }

        // provided e-mail address must be valid
        if (!isValidEmailAddress(user.email)) {
            throw InvalidEmailAddressException(user.email)
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


internal class UserMustBeAnAdultException :
    RuntimeException("User must be 18 years or older to register account")

internal class InvalidEmailAddressException(email: String) :
    RuntimeException("Invalid email address $email")


/*
TODO
IDEAS:
jeśli cena pomiędzy 10 .. 100
 */