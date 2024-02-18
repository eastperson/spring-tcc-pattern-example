package com.ep.api.service.distributed

import com.ep.account.api.data.DepositRequest
import com.ep.account.api.data.WithdrawRequest
import com.ep.account.data.AccountInfo
import com.ep.account.domain.Account
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.PostExchange

interface WithdrawApi {

    @PostExchange(url = "/api/withdraw")
    fun withdraw(@RequestBody withdrawRequest: WithdrawRequest): ResponseEntity<Account>

    @GetExchange(url = "/api/deposit")
    fun query(@RequestParam accountNumber: String): ResponseEntity<AccountInfo>
}