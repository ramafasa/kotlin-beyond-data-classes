package pl.rafalmaciak.ecommerce.user

import java.util.UUID

internal object UserRepository {

    private val users = mutableSetOf<User>()
    var shouldFail: Boolean = false

    fun persist(user: User): UUID {
        if (shouldFail) {
            throw RuntimeException("Failed to persist user")
        }
        users.add(user)

        return UUID.randomUUID()
    }
}