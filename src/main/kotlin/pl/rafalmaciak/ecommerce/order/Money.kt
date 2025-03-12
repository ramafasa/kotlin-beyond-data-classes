package pl.rafalmaciak.ecommerce.order

internal class Money(
    val amount: Double,
    val currency: Currency
) {

    operator fun plus(other: Money): Money {
        check(this.currency == other.currency) {
            "Cannot add money in different currencies: ${this.currency} and ${other.currency}"
        }

        return Money(this.amount + other.amount, currency)
    }

    operator fun times(factor: Double): Money = Money(this.amount * factor, currency)
}

internal enum class Currency {
    PLN, USD, EUR,
}