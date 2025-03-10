package pl.rmaciak.ecommerce.user

internal object UserRepository {

    private val users = mutableSetOf<User>()

    fun persist(user: User) {
        users.add(user)
    }
}