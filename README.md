# Beyond data classes. Kotlin-powered domain modelling

Code for the talk by Rafał Maciak.

## What the talk is about

Kotlin is a great choice for backend services with rich domain models and complex business logic. It lets you write
domain code that is:

- highly readable and concise (because we mainly read the code),
- aligned with the business language,
- less error-prone,
- easy to test,
- resilient to requirement changes,
- more fun to write.

The talk is a live refactoring session: we start from anemic, not very expressive code and turn it, step by step, into
a more readable and idiomatic domain model using Kotlin features.

The example is a (very basic) e-commerce domain:

- user registration,
- order creation and management,
- order export to Excel,
- money operations.

## There are no slides

There is no slide deck for this talk. The whole presentation consists of:

1. **Four intro files** in [`src/main/kotlin/presentation`](src/main/kotlin/presentation), shown straight from the IDE:

   | File                      | Content                      |
   |---------------------------|------------------------------|
   | `1_Title.kt`              | Title                        |
   | `2_AboutPresentation.kt`  | What the presentation is about |
   | `3_AboutMe.kt`            | About the speaker            |
   | `4_Domain.kt`             | The domain used in the example |

2. **Live coding** — everything else happens in the code under
   [`src/main/kotlin/pl/rafalmaciak/ecommerce`](src/main/kotlin/pl/rafalmaciak/ecommerce) and its tests.

`999_WrapUp.kt` closes the session with a summary of the features that were covered.

## Live coding steps

The `main` branch holds the starting point: deliberately anemic code. Each refactoring step has its own branch with the
result of that step.

| Branch                                        | Kotlin feature                          |
|-----------------------------------------------|-----------------------------------------|
| `1.1-email-as-value-class`                    | Value class                             |
| `1.2-range-operator-for-user-age-validation`  | Range operator                          |
| `1.3-user-registration-returning-result`      | `Result` class                          |
| `1.4-sealed-class-as-result`                  | Sealed class as a result                |
| `1.5-factory-method-in-companion-object`      | Factory method in a companion object    |
| `1.6-context-parameters`                      | Context parameters                      |
| `2.1-order-as-sealed-classes`                 | Sealed classes for the order lifecycle  |
| `2.2-default-arguments`                       | Default arguments                       |
| `2.3-extension-function-for-reporting`        | Extension functions                     |
| `3.1-money-with-operator-overloading`         | Operator overloading                    |
| `3.2-integration-tests-with-dsl`              | DSL for tests                           |

## Running

Requires JDK 25 (resolved by the Gradle toolchain).

```shell
./gradlew test
```
