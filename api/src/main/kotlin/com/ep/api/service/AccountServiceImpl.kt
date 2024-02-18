package com.ep.api.service

import com.ep.account.Command
import com.ep.account.data.AccountInfo
import com.ep.api.controller.data.TransferResponse
import com.ep.account.logger
import com.ep.account.service.DepositProcessor
import com.ep.account.service.QueryProcessor
import com.ep.account.service.WithdrawProcessor
import com.ep.api.controller.data.AccountResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class AccountServiceImpl(
    val depositProcessor: DepositProcessor,
    val withdrawProcessor: WithdrawProcessor,
    val queryProcessor: QueryProcessor
) : AccountService {

    val log = logger()

    @Transactional
    override fun transfer(depositAccountNumber: String, withdrawAccountNumber: String, amount: BigDecimal, command: Command): TransferResponse {
        log.info("[TRANSFER] $withdrawAccountNumber => $depositAccountNumber :: $amount :: $command")

        // withdraw
        log.info("[WITHDRAW] call. accountNumber:${withdrawAccountNumber}, amount:${amount}")
        val withdrawAccount = withdrawProcessor.withdraw(withdrawAccountNumber, amount, command)
        log.info("[WITHDRAW] Success. accountNumber:${withdrawAccount.accountNumber}, accountName:${withdrawAccount.accountName}, balance:${withdrawAccount.balance}")

        // deposit
        log.info("[DEPOSIT] call. accountNumber:${withdrawAccountNumber}, amount:${amount}")
        val depositAccount = depositProcessor.deposit(depositAccountNumber, amount, command)
        log.info("[DEPOSIT] Success. accountNumber:${withdrawAccount.accountNumber}, accountName:${withdrawAccount.accountName}, balance:${withdrawAccount.balance}")

        return TransferResponse(
            withdrawAccountInfo = AccountInfo(withdrawAccount.accountNumber, accountName = withdrawAccount.accountName, withdrawAccount.balance),
            depositAccountInfo = AccountInfo(depositAccount.accountNumber, accountName = depositAccount.accountName, depositAccount.balance)
        )
    }

    override fun query(accountNumber: String): AccountResponse {
        val accountInfo = queryProcessor.query(accountNumber)
        return AccountResponse(
            accountNumber = accountInfo.accountNumber,
            accountName = accountInfo.accountName,
            accountBalance = accountInfo.accountBalance
        )
    }
}