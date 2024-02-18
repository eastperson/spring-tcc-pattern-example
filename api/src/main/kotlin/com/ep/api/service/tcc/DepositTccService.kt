package com.ep.api.service.tcc

import com.ep.account.Command
import com.ep.account.api.data.DepositRequest
import com.ep.account.data.CancelResult
import com.ep.account.data.ConfirmResult
import com.ep.account.data.State
import com.ep.account.exception.DepositException
import com.ep.account.logger
import com.ep.api.service.distributed.DepositApi
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.math.BigDecimal

@Service
@Profile("distributed-tcc")
class DepositTccService(
    private val depositApi: DepositApi
) {

    val log = logger()

    fun confirm(tccStateId: Long, depositAccountNumber: String, amount: BigDecimal, command: Command): ConfirmResult {
        val depositRequest = DepositRequest(
            accountNumber = depositAccountNumber,
            amount = amount,
            command = command,
            tccStateId = tccStateId
        )
        try {
            return depositApi.confirm(depositRequest).body!!
        } catch (e: WebClientResponseException) {
            log.error("WebClientResponseException: ", e)
            return ConfirmResult(State.FAIL, DepositException("DepositException"), null)
        }
    }

    fun cancel(tccStateId: Long, depositAccountNumber: String, amount: BigDecimal, command: Command): CancelResult {
        val depositRequest = DepositRequest(
            accountNumber = depositAccountNumber,
            amount = amount,
            command = command,
            tccStateId = tccStateId
        )
        return depositApi.cancel(depositRequest).body!!
    }
}