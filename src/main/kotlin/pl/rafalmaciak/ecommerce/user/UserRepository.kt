package pl.rafalmaciak.ecommerce.user

internal object UserRepository {

    private val users = mutableSetOf<User>()
    var shouldFail: Boolean = false

    fun persist(user: User) {
        if (shouldFail) {
            throw RuntimeException("Failed to persist user")
        }
        users.add(user)
    }
}