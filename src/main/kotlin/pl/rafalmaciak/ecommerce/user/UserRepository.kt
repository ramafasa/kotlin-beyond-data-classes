package pl.rafalmaciak.ecommerce.user

import kotlin.uuid.Uuid

internal object UserRepository {

    private val users = mutableSetOf<User>()
    var shouldFail: Boolean = false

    fun exists(user: User): Boolean =
        users.contains(user)

    fun persist(user: User): Uuid {
        if (shouldFail) {
            throw RuntimeException("Failed to persist user")
        }
        users.add(user)

        return Uuid.random()
    }

    fun clear() {
        users.clear()
    }
}