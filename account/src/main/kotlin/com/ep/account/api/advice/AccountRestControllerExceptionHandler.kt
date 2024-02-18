package com.ep.account.api.advice

import com.ep.account.exception.NotFoundEntity
import com.ep.account.data.AccountInfo
import com.ep.account.exception.DepositException
import com.ep.account.exception.WithdrawException
import com.ep.account.logger
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AccountRestControllerExceptionHandler {

    private val log = logger()

    @ExceptionHandler(NotFoundEntity::class)
    fun notFoundEntityExceptionHandler(request: HttpServletRequest, e: Exception): ResponseEntity<AccountInfo?> {
        log.info("request body: $request, error msg: ${e.message}, exception type: ${e::class}")
        return ResponseEntity.status(HttpStatus.OK)
            .body(null)
    }
}