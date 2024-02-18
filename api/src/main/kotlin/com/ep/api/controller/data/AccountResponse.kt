package com.ep.api.controller.data

import java.math.BigDecimal

data class AccountResponse(
    val accountNumber: String,
    val accountName: String,
    val accountBalance: BigDecimal
)
