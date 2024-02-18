package com.ep.account.service

import com.ep.account.data.AccountInfo
import com.ep.account.exception.NotFoundEntity
import com.ep.account.repository.AccountRepository
import org.springframework.stereotype.Component

@Component
class QueryProcessor(
    private val accountRepository: AccountRepository
) {

    fun query(accountNumber: String): AccountInfo {
        val account = accountRepository.findByAccountNumber(accountNumber).orElseThrow { NotFoundEntity() }
        return AccountInfo(
            accountNumber = account.accountNumber,
            accountName = account.accountName,
            accountBalance = account.balance
        )
    }
}