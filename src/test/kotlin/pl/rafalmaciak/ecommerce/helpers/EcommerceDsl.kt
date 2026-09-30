package pl.rafalmaciak.ecommerce.helpers

/**
 * Marks the test DSL builders. Thanks to @DslMarker, inside a nested block (e.g. `item { }`)
 * members of the outer builder (e.g. `orderCreator`, `item`) cannot be called implicitly.
 */
@DslMarker
internal annotation class EcommerceDsl
