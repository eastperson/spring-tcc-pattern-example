package com.ep.account.service

import com.ep.account.Command
import com.ep.account.domain.Account
import com.ep.account.domain.TccState
import com.ep.account.domain.TccStatus
import com.ep.account.exception.NotFoundEntity
import com.ep.account.exception.WithdrawException
import com.ep.account.logger
import com.ep.account.repository.AccountRepository
import com.ep.account.repository.TccStateRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Component
class WithdrawProcessor(
    private val accountRepository: AccountRepository,
    private val tccStateRepository: TccStateRepository
) {

    val log = logger()

    @Transactional
    fun withdraw(withdrawAccountNumber: String, amount: BigDecimal, command: Command): Account {
        if (command == Command.WITHDRAW_EXCEPTION) {
            throw WithdrawException("WithdrawException")
        }

        val withdrawAccount = accountRepository.findByAccountNumber(withdrawAccountNumber).orElseThrow { IllegalStateException() }
        validate(withdrawAccount, amount)
        log.info("[WITHDRAW] before account accountNumber:${withdrawAccount.accountNumber}, accountName:${withdrawAccount.accountName}, balance:${withdrawAccount.balance}")
        withdrawAccount.withdraw(amount)
        log.info("[WITHDRAW] after account accountNumber:${withdrawAccount.accountNumber}, accountName:${withdrawAccount.accountName}, balance:${withdrawAccount.balance}")
        return withdrawAccount
    }

    @Transactional
    fun withdrawWithTcc(tccStateId: Long, withdrawAccountNumber: String, amount: BigDecimal, command: Command): Account {
        tccStateRepository.save(TccState(tccStateId, TccStatus.CONFIRM, LocalDateTime.now()))
        return withdraw(withdrawAccountNumber, amount, command)
    }

    private fun validate(withdrawAccount: Account, amount: BigDecimal) {
        if (withdrawAccount.balance < amount) {
            throw WithdrawException("WithdrawException")
        }
    }

    @Transactional
    fun rollback(tccStateId: Long, accountNumber: String, amount: BigDecimal) {
        validate(tccStateId)
        val withdrawAccount = accountRepository.findByAccountNumber(accountNumber).orElseThrow { WithdrawException("WithdrawException") }
        log.info("[WITHDRAW ROLLBACK] before account accountNumber:${withdrawAccount.accountNumber}, accountName:${withdrawAccount.accountName}, balance:${withdrawAccount.balance}")
        withdrawAccount.deposit(amount)
        log.info("[WITHDRAW ROLLBACK] after account accountNumber:${withdrawAccount.accountNumber}, accountName:${withdrawAccount.accountName}, balance:${withdrawAccount.balance}")
        tccStateRepository.save(TccState(tccStateId, TccStatus.CANCEL, LocalDateTime.now()))
    }

    private fun validate(tccStateId: Long) {
        tccStateRepository.findByIdAndStatus(tccStateId, TccStatus.CONFIRM).orElseThrow {
            log.info("[WITHDRAW ROLLBACK] not execute.")
            NotFoundEntity()
        }
    }
}