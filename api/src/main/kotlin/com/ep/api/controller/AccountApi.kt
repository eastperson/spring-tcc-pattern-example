package com.ep.api.controller

import com.ep.api.controller.data.AccountResponse
import com.ep.api.controller.data.TransferRequest
import com.ep.api.controller.data.TransferResponse
import com.ep.api.service.AccountService
import com.ep.api.service.AccountTccService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/account")
class AccountApi(
    private val accountService: AccountService,
    private val accountTccService: AccountTccService
) {

    @PostMapping("/transfer")
    fun transfer(@RequestBody transferRequest: TransferRequest): ResponseEntity<TransferResponse> {
        val response = accountService.transfer(
            transferRequest.depositAccountNumber,
            transferRequest.withdrawAccountNumber,
            transferRequest.amount,
            transferRequest.command
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping("/transfer-tcc")
    fun transferTcc(@RequestBody transferRequest: TransferRequest): ResponseEntity<TransferResponse> {
        val response = accountTccService.transfer(
            transferRequest.depositAccountNumber,
            transferRequest.withdrawAccountNumber,
            transferRequest.amount,
            transferRequest.command
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{accountNumber}")
    fun query(@PathVariable accountNumber: String): ResponseEntity<AccountResponse> {
        val response = accountService.query(accountNumber)
        return ResponseEntity.ok(response)
    }
}