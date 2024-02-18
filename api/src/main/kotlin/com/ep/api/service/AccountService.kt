package com.ep.api.service

import com.ep.account.Command
import com.ep.api.controller.data.AccountResponse
import com.ep.api.controller.data.TransferResponse
import java.math.BigDecimal

interface AccountService {
    fun transfer(depositAccountNumber: String, withdrawAccountNumber: String, amount: BigDecimal, command: Command): TransferResponse
    fun query(accountNumber: String): AccountResponse
}