package com.ep.api.service

import com.ep.account.Command
import com.ep.account.domain.Account
import java.math.BigDecimal

interface DepositService {

    fun deposit(depositAccountNumber: String, amount: BigDecimal, command: Command): Account
}