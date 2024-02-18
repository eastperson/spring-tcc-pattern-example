package com.ep.api

import com.ep.account.domain.Account
import com.ep.account.repository.AccountRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Component
import java.math.BigDecimal

@EnableJpaRepositories(basePackages = ["com.ep"])
@EntityScan(basePackages = ["com.ep"])
@SpringBootApplication(scanBasePackages = ["com.ep"])
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}

@Component
class Initializer(
    private val accountRepository: AccountRepository
) : InitializingBean {
    override fun afterPropertiesSet() {
        val accountA = Account(accountNumber = "12345678", accountName = "ep", balance = BigDecimal.valueOf(1_000_000))
        val accountB = Account(accountNumber = "98765432", accountName = "jhon", balance = BigDecimal.valueOf(100_000))
        accountRepository.save(accountA)
        accountRepository.save(accountB)
    }
}
