package com.ep.api.service

import com.ep.account.Command
import com.ep.account.service.DepositProcessor
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class DepositServiceImpl(
    private val depositProcessor: DepositProcessor
) : DepositService {
    override fun deposit(depositAccountNumber: String, amount: BigDecimal, command: Command)
        = depositProcessor.deposit(depositAccountNumber, amount, command)
}