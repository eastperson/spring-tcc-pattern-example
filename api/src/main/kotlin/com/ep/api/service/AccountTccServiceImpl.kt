package com.ep.api.service

import com.ep.account.Command
import com.ep.account.data.AccountInfo
import com.ep.account.logger
import com.ep.api.controller.data.TransferResponse
import com.ep.api.service.tcc.DepositTccService
import com.ep.account.domain.TccState
import com.ep.account.domain.TccStatus
import com.ep.account.data.TccResult
import com.ep.api.service.tcc.TccStateService
import com.ep.account.data.TryResult
import com.ep.api.service.tcc.WithdrawTccService
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
@Profile("distributed-tcc")
class AccountTccServiceImpl(
    val depositService: DepositTccService,
    val withdrawService: WithdrawTccService,
    val tccStateService: TccStateService
) : AccountTccService {

    val log = logger()

    override fun transfer(depositAccountNumber: String, withdrawAccountNumber: String, amount: BigDecimal, command: Command): TransferResponse {
        log.info("[TRANSFER] $withdrawAccountNumber => $depositAccountNumber :: $amount :: $command")

        val tccResult = TccResult(TccState(status = TccStatus.INIT, created = LocalDateTime.now()))
        saveTcc(tccResult)

        // try
        tryLogic(withdrawAccountNumber, amount, command, tccResult)

        // confirm
        if (tccResult.isTrySuccess()) {
            confirmLogic(depositAccountNumber, amount, command, tccResult)
        }

        // cancel
        if (!tccResult.isConfirmSuccess()) {
            cancelLogic(withdrawAccountNumber, depositAccountNumber, amount, command, tccResult)
            throw tccResult.exception!!
        }

        val withdrawAccount = tccResult.withdrawAccount!!
        val depositAccount = tccResult.depositAccount!!

        return TransferResponse(
            withdrawAccountInfo = AccountInfo(withdrawAccount.accountNumber, accountName = withdrawAccount.accountName, withdrawAccount.balance),
            depositAccountInfo = AccountInfo(depositAccount.accountNumber, accountName = depositAccount.accountName, depositAccount.balance)
        )
    }

    private fun saveTcc(tccResult: TccResult) {
        tccStateService.save(tccResult.tccState)
    }

    private fun tryLogic(withdrawAccountNumber: String, amount: BigDecimal, command: Command, tccResult: TccResult): TryResult {
        log.info("[WITHDRAW] call. accountNumber:${withdrawAccountNumber}, amount:${amount}")
        val result = withdrawService.tryLogic(tccResult.tccState.id!!, withdrawAccountNumber, amount, command)
        tccResult.applyResult(result)

        if (tccResult.isTrySuccess()) {
            val withdrawAccount = tccResult.withdrawAccount!!
            log.info("[WITHDRAW] Success. accountNumber:${withdrawAccount.accountNumber}, accountName:${withdrawAccount.accountName}, balance:${withdrawAccount.balance}")
        } else {
            log.info("[WITHDRAW] Fail.")
        }
        saveTcc(tccResult)
        return result
    }

    private fun confirmLogic(depositAccountNumber: String, amount: BigDecimal, command: Command, tccResult: TccResult) {
        log.info("[DEPOSIT] call. accountNumber:${depositAccountNumber}, amount:${amount}")
        val result = depositService.confirm(tccResult.tccState.id!!, depositAccountNumber, amount, command)
        tccResult.applyResult(result)

        if (tccResult.isConfirmSuccess()) {
            val depositAccount = tccResult.depositAccount!!
            log.info("[DEPOSIT] Success. accountNumber:${depositAccount.accountNumber}, accountName:${depositAccount.accountName}, balance:${depositAccount.balance}")
        } else {
            log.info("[DEPOSIT] Fail.")
        }
        saveTcc(tccResult)
    }

    private fun cancelLogic(
        withdrawAccountNumber: String,
        depositAccountNumber: String,
        amount: BigDecimal,
        command: Command,
        tccResult: TccResult
    ) {
        log.info("[CANCEL] call. withdrawAccountNumber:${withdrawAccountNumber}, depositAccountNumber:${depositAccountNumber}, amount:${amount}")
        val result = depositService.cancel(tccResult.tccState.id!!, depositAccountNumber, amount, command)
        val result2 = withdrawService.cancel(tccResult.tccState.id!!, withdrawAccountNumber, amount, command)
        tccResult.applyResult(result, result2)

        log.info("[CANCEL] Success. withdrawAccountNumber:${withdrawAccountNumber}, depositAccountNumber:${depositAccountNumber}, amount:${amount}")
        saveTcc(tccResult)
    }
}