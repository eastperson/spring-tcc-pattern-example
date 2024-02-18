package com.ep.account.service

import com.ep.account.Command
import com.ep.account.domain.Account
import com.ep.account.exception.DepositException
import com.ep.account.logger
import com.ep.account.repository.AccountRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Component
class DepositProcessor(
    private val accountRepository: AccountRepository
) {

    val log = logger()

    @Transactional
    fun deposit(depositAccountNumber: String, amount: BigDecimal, command: Command): Account {
        if (command == Command.DEPOSIT_EXCEPTION) {
            throw DepositException()
        }

        val depositAccount = accountRepository.findByAccountNumber(depositAccountNumber).orElseThrow { IllegalStateException() }
        log.info("[DEPOSIT] before account accountNumber:${depositAccount.accountNumber}, accountName:${depositAccount.accountName}, balance:${depositAccount.balance}")
        depositAccount.deposit(amount)
        log.info("[DEPOSIT] after account accountNumber:${depositAccount.accountNumber}, accountName:${depositAccount.accountName}, balance:${depositAccount.balance}")
        return depositAccount
    }
}