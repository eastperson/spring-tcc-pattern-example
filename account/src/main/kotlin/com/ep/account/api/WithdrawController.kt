package com.ep.account.api

import com.ep.account.api.data.DepositRequest
import com.ep.account.api.data.WithdrawRequest
import com.ep.account.data.AccountInfo
import com.ep.account.domain.Account
import com.ep.account.service.DepositProcessor
import com.ep.account.service.QueryProcessor
import com.ep.account.service.WithdrawProcessor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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

    @GetMapping
    fun query(@RequestParam accountNumber: String): ResponseEntity<AccountInfo> {
        return ResponseEntity.ok(queryProcessor.query(accountNumber))
    }
}