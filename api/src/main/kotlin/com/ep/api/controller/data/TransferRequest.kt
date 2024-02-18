package com.ep.api.controller.data

import com.ep.account.Command
import java.math.BigDecimal

data class TransferRequest(
    val withdrawAccountNumber: String,
    val depositAccountNumber: String,
    val amount: BigDecimal,
    val command: Command
)
