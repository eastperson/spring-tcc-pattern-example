package com.ep.account.api

import com.ep.account.api.data.DepositRequest
import com.ep.account.api.data.WithdrawRequest
import com.ep.account.data.AccountInfo
import com.ep.account.data.CancelResult
import com.ep.account.data.ConfirmResult
import com.ep.account.data.State
import com.ep.account.data.TryResult
import com.ep.account.domain.Account
import com.ep.account.exception.DepositException
import com.ep.account.exception.NotFoundEntity
import com.ep.account.exception.WithdrawException
import com.ep.account.service.DepositProcessor
import com.ep.account.service.QueryProcessor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/deposit")
class DepositController(
    private val depositProcessor: DepositProcessor,
    private val queryProcessor: QueryProcessor
) {

    @PostMapping
    fun deposit(@RequestBody depositRequest: DepositRequest): ResponseEntity<Account> {
        return ResponseEntity.ok(depositProcessor.deposit(depositRequest.accountNumber, depositRequest.amount, depositRequest.command))
    }

    @PostMapping("/confirm")
    fun confirm(@RequestBody depositRequest: DepositRequest): ResponseEntity<ConfirmResult> {
        try {
            val account = depositProcessor.depositWithTcc(depositRequest.tccStateId!!, depositRequest.accountNumber, depositRequest.amount, depositRequest.command)
            return ResponseEntity.ok(ConfirmResult(State.SUCCESS, null, account))
        } catch (e: DepositException) {
            return ResponseEntity.ok(ConfirmResult(State.FAIL, e, null))
        }
    }

    @PostMapping("/cancel")
    fun cancel(@RequestBody depositRequest: DepositRequest): ResponseEntity<CancelResult> {
        try {
            depositProcessor.rollback(depositRequest.tccStateId!!, depositRequest.accountNumber, depositRequest.amount)
            return ResponseEntity.ok(CancelResult(State.SUCCESS, null))
        } catch (e: NotFoundEntity) {
            return ResponseEntity.ok(CancelResult(State.SUCCESS, null))
        } catch (e: DepositException) {
            return ResponseEntity.ok(CancelResult(State.FAIL, e))
        }
    }

    @GetMapping
    fun query(@RequestParam accountNumber: String): ResponseEntity<AccountInfo> {
        return ResponseEntity.ok(queryProcessor.query(accountNumber))
    }
}