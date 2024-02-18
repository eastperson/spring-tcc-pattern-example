package com.ep.account.api

import com.ep.account.api.data.DepositRequest
import com.ep.account.api.data.WithdrawRequest
import com.ep.account.data.AccountInfo
import com.ep.account.data.CancelResult
import com.ep.account.data.ConfirmResult
import com.ep.account.data.State
import com.ep.account.data.TryResult
import com.ep.account.domain.Account
import com.ep.account.exception.NotFoundEntity
import com.ep.account.exception.WithdrawException
import com.ep.account.service.DepositProcessor
import com.ep.account.service.QueryProcessor
import com.ep.account.service.WithdrawProcessor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/withdraw")
class WithdrawController(
    private val withdrawProcessor: WithdrawProcessor,
    private val queryProcessor: QueryProcessor
) {

    @PostMapping
    fun withdraw(@RequestBody withdrawRequest: WithdrawRequest): ResponseEntity<Account> {
        return ResponseEntity.ok(withdrawProcessor.withdraw(withdrawRequest.accountNumber, withdrawRequest.amount, withdrawRequest.command))
    }

    @PostMapping("/try")
    fun tryLogic(@RequestBody withdrawRequest: WithdrawRequest): ResponseEntity<TryResult> {
        try {
            val account = withdrawProcessor.withdrawWithTcc(withdrawRequest.tccStateId!!, withdrawRequest.accountNumber, withdrawRequest.amount, withdrawRequest.command)
            return ResponseEntity.ok(TryResult(State.SUCCESS, null, account))
        } catch (e: WithdrawException) {
            return ResponseEntity.ok(TryResult(State.FAIL, e, null))
        }
    }

    @PostMapping("/cancel")
    fun cancel(@RequestBody withdrawRequest: WithdrawRequest): ResponseEntity<CancelResult> {
        try {
            withdrawProcessor.rollback(withdrawRequest.tccStateId!!, withdrawRequest.accountNumber, withdrawRequest.amount)
            return ResponseEntity.ok(CancelResult(State.SUCCESS, null))
        } catch (e: NotFoundEntity) {
            return ResponseEntity.ok(CancelResult(State.SUCCESS, null))
        } catch (e: WithdrawException) {
            return ResponseEntity.ok(CancelResult(State.FAIL, e))
        }
    }

    @GetMapping
    fun query(@RequestParam accountNumber: String): ResponseEntity<AccountInfo> {
        return ResponseEntity.ok(queryProcessor.query(accountNumber))
    }
}