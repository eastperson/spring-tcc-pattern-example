package com.ep.api.service.distributed

import com.ep.account.Command
import com.ep.account.api.data.DepositRequest
import com.ep.account.api.data.WithdrawRequest
import com.ep.account.domain.Account
import com.ep.account.service.WithdrawProcessor
import com.ep.api.controller.data.AccountResponse
import com.ep.api.service.DepositService
import com.ep.api.service.WithdrawService
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
@Profile("distributed")
class WithdrawDistributedService(
    private val withdrawApi: WithdrawApi
) : WithdrawService {
    override fun withdraw(withdrawAccountNumber: String, amount: BigDecimal, command: Command): Account {
        val withdrawRequest = WithdrawRequest(withdrawAccountNumber, amount, command)
        val account = withdrawApi.withdraw(withdrawRequest).body
        return account ?: throw IllegalArgumentException()
    }

    override fun query(accountNumber: String): AccountResponse? {
        val accountInfo = withdrawApi.query(accountNumber).body
        return accountInfo?.let { AccountResponse(
            accountNumber = accountInfo.accountNumber,
            accountName = accountInfo.accountName,
            accountBalance = accountInfo.accountBalance
        )}
    }
}