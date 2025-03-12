package pl.rafalmaciak.ecommerce.order

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.doubles.shouldBeExactly
import io.kotest.matchers.shouldBe
import pl.rafalmaciak.ecommerce.order.Currency.PLN
import pl.rafalmaciak.ecommerce.order.Currency.USD

class MoneyTest : ShouldSpec({

    should("add money in the same currency") {
        val money1 = Money(100.0, PLN)
        val money2 = Money(200.0, PLN)
        val result = money1.add(money2)

        result.amount shouldBeExactly 300.0
        result.currency shouldBe PLN
    }

    should("throw exception when adding money with different currency") {
        val money1 = Money(100.0, PLN)
        val money2 = Money(200.0, USD)

        shouldThrow<IllegalStateException> {
            money1.add(money2)
        }
    }

    should("apply discount to money") {
        val money = Money(100.0, PLN)
        val result = money.multiply(0.23)
        result.amount shouldBeExactly 23.0
        result.currency shouldBe PLN
    }
})