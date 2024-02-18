package com.ep.account.api.data

import com.ep.account.Command
import java.math.BigDecimal

data class WithdrawRequest(
    val accountNumber: String,
    val amount: BigDecimal,
    val command: Command
)