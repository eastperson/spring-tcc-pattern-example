package com.ep.api.service.distributed

import com.ep.account.api.data.DepositRequest
import com.ep.account.data.AccountInfo
import com.ep.account.domain.Account
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.PostExchange

interface DepositApi {

    @PostExchange(url = "/api/deposit")
    fun deposit(@RequestBody depositRequest: DepositRequest): ResponseEntity<Account>

    @GetExchange(url = "/api/deposit")
    fun query(@RequestParam accountNumber: String): ResponseEntity<AccountInfo>
}