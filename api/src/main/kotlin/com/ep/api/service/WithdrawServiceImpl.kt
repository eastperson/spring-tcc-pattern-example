package com.ep.api.service

import com.ep.account.Command
import com.ep.account.service.WithdrawProcessor
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class WithdrawServiceImpl(
    private val withdrawProcessor: WithdrawProcessor
) : WithdrawService {
    override fun withdraw(withdrawAccountNumber: String, amount: BigDecimal, command: Command)
        = withdrawProcessor.withdraw(withdrawAccountNumber, amount, command)
}