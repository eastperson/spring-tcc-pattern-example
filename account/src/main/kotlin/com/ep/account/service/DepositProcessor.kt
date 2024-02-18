package com.ep.account.service

import com.ep.account.Command
import com.ep.account.domain.Account
import com.ep.account.domain.TccState
import com.ep.account.domain.TccStatus
import com.ep.account.exception.DepositException
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
class DepositProcessor(
    private val accountRepository: AccountRepository,
    private val tccStateRepository: TccStateRepository
) {

    val log = logger()

    @Transactional
    fun deposit(depositAccountNumber: String, amount: BigDecimal, command: Command): Account {
        if (command == Command.DEPOSIT_EXCEPTION) {
            throw DepositException("DepositException")
        }

        val depositAccount = accountRepository.findByAccountNumber(depositAccountNumber).orElseThrow { IllegalStateException() }
        log.info("[DEPOSIT] before account accountNumber:${depositAccount.accountNumber}, accountName:${depositAccount.accountName}, balance:${depositAccount.balance}")
        depositAccount.deposit(amount)
        log.info("[DEPOSIT] after account accountNumber:${depositAccount.accountNumber}, accountName:${depositAccount.accountName}, balance:${depositAccount.balance}")
        return depositAccount
    }

    @Transactional
    fun depositWithTcc(tccStateId: Long, depositAccountNumber: String, amount: BigDecimal, command: Command): Account {
        tccStateRepository.save(TccState(tccStateId, TccStatus.CONFIRM, LocalDateTime.now()))
        return deposit(depositAccountNumber, amount, command)
    }

    @Transactional
    fun rollback(tccStateId: Long, accountNumber: String, amount: BigDecimal) {
        validate(tccStateId)
        val depositAccount = accountRepository.findByAccountNumber(accountNumber).orElseThrow { DepositException("DepositException") }
        log.info("[DEPOSIT ROLLBACK] before account accountNumber:${depositAccount.accountNumber}, accountName:${depositAccount.accountName}, balance:${depositAccount.balance}")
        depositAccount.withdraw(amount)
        log.info("[DEPOSIT ROLLBACK] after account accountNumber:${depositAccount.accountNumber}, accountName:${depositAccount.accountName}, balance:${depositAccount.balance}")
        tccStateRepository.save(TccState(tccStateId, TccStatus.CANCEL, LocalDateTime.now()))
    }

    private fun validate(tccStateId: Long) {
        tccStateRepository.findByIdAndStatus(tccStateId, TccStatus.CONFIRM).orElseThrow {
            log.info("[DEPOSIT ROLLBACK] not execute.")
            NotFoundEntity()
        }
    }
}