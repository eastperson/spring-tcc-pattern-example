package com.ep.api.service.monolithic

import com.ep.account.Command
import com.ep.account.service.DepositProcessor
import com.ep.account.service.QueryProcessor
import com.ep.api.controller.data.AccountResponse
import com.ep.api.service.DepositService
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
@Profile("monolithic")
class DepositMonolithicService(
    private val depositProcessor: DepositProcessor,
    private val queryProcessor: QueryProcessor
) : DepositService {
    override fun deposit(depositAccountNumber: String, amount: BigDecimal, command: Command)
        = depositProcessor.deposit(depositAccountNumber, amount, command)

    override fun query(accountNumber: String): AccountResponse? {
        try {
            val accountInfo = queryProcessor.query(accountNumber)
            return AccountResponse(
                accountNumber = accountInfo.accountNumber,
                accountName = accountInfo.accountName,
                accountBalance = accountInfo.accountBalance
            )
        } catch (e: IllegalArgumentException) {
            return null
        }
    }
}