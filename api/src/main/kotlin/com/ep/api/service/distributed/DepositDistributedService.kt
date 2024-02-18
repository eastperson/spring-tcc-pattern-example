package com.ep.api.service.distributed

import com.ep.account.Command
import com.ep.account.api.data.DepositRequest
import com.ep.account.domain.Account
import com.ep.account.exception.DepositException
import com.ep.account.logger
import com.ep.account.service.DepositProcessor
import com.ep.api.controller.data.AccountResponse
import com.ep.api.service.DepositService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.math.BigDecimal

@Service
@Profile("distributed", "distributed-tcc")
class DepositDistributedService(
    private val depositApi: DepositApi
) : DepositService {

    val log = logger()

    override fun deposit(depositAccountNumber: String, amount: BigDecimal, command: Command): Account {
        val depositRequest = DepositRequest(depositAccountNumber, amount, command, null)
        try {
            val account = depositApi.deposit(depositRequest).body
            return account ?: throw DepositException("DepositException")
        } catch (e: WebClientResponseException) {
            log.error("WebClientResponseException: ", e)
            throw DepositException("DepositException")
        }
    }

    override fun query(accountNumber: String): AccountResponse? {
        val accountInfo = depositApi.query(accountNumber).body
        return accountInfo?.let { AccountResponse(
            accountNumber = accountInfo.accountNumber,
            accountName = accountInfo.accountName,
            accountBalance = accountInfo.accountBalance
        )}
    }
}