package pl.rafalmaciak.ecommerce.user

import kotlin.uuid.Uuid

internal interface UserRepository {
    fun exists(user: User): Boolean
    fun persist(user: User): Uuid
}

internal class InMemoryUserRepository : UserRepository {

    private val users = mutableSetOf<User>()

    override fun exists(user: User): Boolean =
        users.contains(user)

    override fun persist(user: User): Uuid {
        users.add(user)

        return Uuid.random()
    }
}
