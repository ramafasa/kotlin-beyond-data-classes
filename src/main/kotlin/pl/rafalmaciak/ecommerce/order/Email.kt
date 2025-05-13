package pl.rafalmaciak.ecommerce.order


/**
 * The @JvmInline annotation is required to declare a value class as an inline class on the JVM.
 * This tells the Kotlin compiler that the class should be optimized to avoid the overhead of
 * object allocation by inlining its single property wherever possible. It ensures that the value
 * class is treated as a primitive-like type on the JVM, which can improve performance and reduce memory overhead.
 */

@JvmInline
internal value class Email(val raw: String) {
    init {
        if (!raw.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))) {
            throw InvalidEmailAddressException(raw)
        }
    }
}

internal class InvalidEmailAddressException(email: String) :
    RuntimeException("Invalid email address $email")