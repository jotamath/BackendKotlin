package io.github.jotamath.model

data class LoanResponse(
    val customer: String,
    val loans: List<Loan>
)

data class Loan(
    val type: String,
    val taxes: Int
)