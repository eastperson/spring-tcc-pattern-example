package com.ep.account.service

import com.ep.account.Command
import com.ep.account.domain.Account
import com.ep.account.exception.WithdrawException
import com.ep.account.logger
import com.ep.account.repository.AccountRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Component
class WithdrawProcessor(
    private val accountRepository: AccountRepository
) {

    val log = logger()

    @Transactional
    fun withdraw(withdrawAccountNumber: String, amount: BigDecimal, command: Command): Account {
        if (command == Command.WITHDRAW_EXCEPTION) {
            throw WithdrawException()
        }

        val withdrawAccount = accountRepository.findByAccountNumber(withdrawAccountNumber).orElseThrow { IllegalStateException() }
        log.info("[WITHDRAW] before account accountNumber:${withdrawAccount.accountNumber}, accountName:${withdrawAccount.accountName}, balance:${withdrawAccount.balance}")
        withdrawAccount.withdraw(amount)
        log.info("[WITHDRAW] after account accountNumber:${withdrawAccount.accountNumber}, accountName:${withdrawAccount.accountName}, balance:${withdrawAccount.balance}")
        return withdrawAccount
    }
}