package com.ep.account.api

import com.ep.account.api.data.DepositRequest
import com.ep.account.data.AccountInfo
import com.ep.account.domain.Account
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

    @GetMapping
    fun query(@RequestParam accountNumber: String): ResponseEntity<AccountInfo> {
        return ResponseEntity.ok(queryProcessor.query(accountNumber))
    }
}