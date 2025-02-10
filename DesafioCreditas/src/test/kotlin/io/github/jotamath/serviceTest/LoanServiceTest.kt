package io.github.jotamath.serviceTest

import io.github.jotamath.model.Customer
import io.github.jotamath.service.LoanService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class LoanServiceTest {

    private val loanService = LoanService()

    @Test
    fun `deve retornar apenas emprestimo pessoal para cliente com salario 3000, BH e 29 anos`() {
        val customer = Customer("Erikaya", "123", 29, "BH", 3000)
        val loans = loanService.calculateAvailableLoans(customer)
        assertEquals(1, loans.size)
        assertEquals("personal", loans[0].type)
    }

    @Test
    fun `deve retornar pessoal e garantia para cliente SP com salario 4000`() {

        val customer = Customer("Teste", "456", 30, "SP", 4000)
        val loans = loanService.calculateAvailableLoans(customer)
        assertEquals(2, loans.size)
        assertTrue(loans.any { it.type == "guaranteed" })
    }

    @Test
    fun `deve retornar todos os emprestimos para cliente com salario 5000 e 25 anos`() {
        val customer = Customer("Jovem", "789", 25, "MG", 5000)
        val loans = loanService.calculateAvailableLoans(customer)
        assertEquals(3, loans.size)
    }
}