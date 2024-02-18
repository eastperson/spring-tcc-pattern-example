package com.ep.api.service.monolithic

import com.ep.account.Command
import com.ep.account.service.QueryProcessor
import com.ep.account.service.WithdrawProcessor
import com.ep.api.controller.data.AccountResponse
import com.ep.api.service.WithdrawService
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
@Profile("monolithic")
class WithdrawMonolithicService(
    private val withdrawProcessor: WithdrawProcessor,
    private val queryProcessor: QueryProcessor
) : WithdrawService {
    override fun withdraw(withdrawAccountNumber: String, amount: BigDecimal, command: Command)
        = withdrawProcessor.withdraw(withdrawAccountNumber, amount, command)

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