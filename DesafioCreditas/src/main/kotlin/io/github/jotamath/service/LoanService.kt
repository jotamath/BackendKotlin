package io.github.jotamath.service

import io.github.jotamath.model.Customer
import io.github.jotamath.model.Loan
import jakarta.inject.Singleton

@Singleton
class LoanService {
    fun calculateAvailableLoans(customer: Customer): List<Loan> {
        val loans = mutableListOf<Loan>()
        //Empréstimo pessoal sempre está disponível
        loans.add(Loan("personal", 1))

        //Verifica empréstimo com garantia
        if (isGuaranteedLoanAvailable(customer)) {
            loans.add(Loan("guaranteed", 2))
        }

        //Verifica consignado
        if (isConsignmentLoanAvailable(customer)) {
            loans.add(Loan("consignment", 3))
        }

        return loans
    }

    private fun isGuaranteedLoanAvailable(customer: Customer): Boolean {
        return when {
            customer.income <= 3000 -> customer.age < 30 && customer.location == "SP"
            customer.income < 5000 -> customer.location == "SP"
            else -> customer.age < 30
        }
    }

    private fun isConsignmentLoanAvailable(customer: Customer): Boolean {
        return customer.income >= 5000
    }
}