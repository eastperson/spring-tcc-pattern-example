package com.ep.account.data

import java.math.BigDecimal

data class AccountInfo(
    val accountNumber: String,
    val accountName: String,
    val accountBalance: BigDecimal
)