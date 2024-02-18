package com.ep.api.service

import com.ep.account.Command
import com.ep.api.controller.data.TransferResponse
import java.math.BigDecimal

interface TransferLogic {
    fun transfer(depositAccountNumber: String, withdrawAccountNumber: String, amount: BigDecimal, command: Command): TransferResponse
}