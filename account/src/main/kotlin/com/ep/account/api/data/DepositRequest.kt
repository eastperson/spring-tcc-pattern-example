package com.ep.account.api.data

import com.ep.account.Command
import java.math.BigDecimal

data class DepositRequest(
    val accountNumber: String,
    val amount: BigDecimal,
    val command: Command
)