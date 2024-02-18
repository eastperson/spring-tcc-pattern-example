package com.ep.api.service.tcc

import com.ep.account.Command
import com.ep.account.api.data.WithdrawRequest
import com.ep.account.data.CancelResult
import com.ep.account.data.State
import com.ep.account.data.TryResult
import com.ep.account.exception.WithdrawException
import com.ep.account.logger
import com.ep.api.service.distributed.WithdrawApi
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.math.BigDecimal

@Service
@Profile("distributed-tcc")
class WithdrawTccService(
    private val withdrawApi: WithdrawApi
){

    val log = logger()

    fun tryLogic(tccStateId: Long, withdrawAccountNumber: String, amount: BigDecimal, command: Command): TryResult {
        val withdrawRequest = WithdrawRequest(
            accountNumber = withdrawAccountNumber,
            amount = amount,
            command = command,
            tccStateId = tccStateId
        )
        try {
            return withdrawApi.tryLogic(withdrawRequest).body!!
        } catch (e: WebClientResponseException) {
            log.error("WebClientResponseException: ", e)
            return TryResult(State.FAIL, WithdrawException("WithdrawException"), null)
        }
    }

    fun cancel(tccStateId: Long, withdrawAccountNumber: String, amount: BigDecimal, command: Command): CancelResult {
        val withdrawRequest = WithdrawRequest(
            accountNumber = withdrawAccountNumber,
            amount = amount,
            command = command,
            tccStateId = tccStateId
        )
        return withdrawApi.cancel(withdrawRequest).body!!
    }
}