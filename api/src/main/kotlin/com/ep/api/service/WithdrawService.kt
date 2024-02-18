package com.ep.api.service

import com.ep.account.Command
import com.ep.account.domain.Account
import java.math.BigDecimal

interface WithdrawService {

    fun withdraw(withdrawAccountNumber: String, amount: BigDecimal, command: Command): Account
}