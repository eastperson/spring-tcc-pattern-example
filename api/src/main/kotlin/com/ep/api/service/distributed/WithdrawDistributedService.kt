package com.ep.api.service.distributed

import com.ep.account.Command
import com.ep.account.api.data.WithdrawRequest
import com.ep.account.domain.Account
import com.ep.account.exception.DepositException
import com.ep.account.exception.WithdrawException
import com.ep.account.logger
import com.ep.api.controller.data.AccountResponse
import com.ep.api.service.WithdrawService
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.math.BigDecimal

@Service
@Profile("distributed", "distributed-tcc")
class WithdrawDistributedService(
    private val withdrawApi: WithdrawApi
) : WithdrawService {

    val log = logger()

    override fun withdraw(withdrawAccountNumber: String, amount: BigDecimal, command: Command): Account {
        val withdrawRequest = WithdrawRequest(withdrawAccountNumber, amount, command, null)
        try {
            val account = withdrawApi.withdraw(withdrawRequest).body
            return account ?: throw WithdrawException("WithdrawException")
        } catch (e: WebClientResponseException) {
            log.error("WebClientResponseException: ", e)
            throw WithdrawException("WithdrawException")
        }
    }

    override fun query(accountNumber: String): AccountResponse? {
        val accountInfo = withdrawApi.query(accountNumber).body
        return accountInfo?.let { AccountResponse(
            accountNumber = accountInfo.accountNumber,
            accountName = accountInfo.accountName,
            accountBalance = accountInfo.accountBalance)
        }
    }
}