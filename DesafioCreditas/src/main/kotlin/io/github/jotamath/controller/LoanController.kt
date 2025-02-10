package io.github.jotamath.controller

import io.github.jotamath.model.Customer
import io.github.jotamath.model.LoanResponse
import io.github.jotamath.service.LoanService
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import jakarta.inject.Inject

@Controller
class LoanController(@Inject private val loanService: LoanService) {

    @Post("/loan")
    fun calculateLoans(@Body request: Map<String, Customer>): LoanResponse {
        val customer = request["customer"]!!
        val loans = loanService.calculateAvailableLoans(customer)
        return LoanResponse(customer.name, loans)
    }
}