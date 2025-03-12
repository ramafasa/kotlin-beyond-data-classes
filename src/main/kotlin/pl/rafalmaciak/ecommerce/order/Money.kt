package pl.rafalmaciak.ecommerce.order

internal class Money(
    val amount: Double,
    val currency: Currency
) {

    fun add(other: Money): Money {
        check(this.currency == other.currency) {
            "Cannot add money in different currencies: ${this.currency} and ${other.currency}"
        }

        return Money(this.amount + other.amount, currency)
    }

    fun multiply(factor: Double): Money {
        return Money(amount * factor, currency)
    }
}

internal enum class Currency {
    PLN, USD, EUR,
}