package com.ep.api

import com.ep.account.repository.AccountRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@EnableTransactionManagement
class MultipleJpaTest {
    @Autowired
    lateinit var accountRepository: AccountRepository

}
