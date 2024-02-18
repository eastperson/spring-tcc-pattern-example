package com.ep.api.controller.advice

import com.ep.account.exception.DepositException
import com.ep.account.exception.WithdrawException
import com.ep.account.logger
import com.ep.api.controller.data.Response
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestControllerExceptionHandler {

    private val log = logger()

    @ExceptionHandler(DepositException::class)
    fun depositExceptionHandler(request: HttpServletRequest, e: Exception): ResponseEntity<Response> {
        log.info("request body: $request, error msg: ${e.message}, exception type: ${e::class}")
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                Response(
                    message = "${DepositException::class.simpleName}"
                )
            )
    }

    @ExceptionHandler(WithdrawException::class)
    fun withdrawExceptionHandler(request: HttpServletRequest, e: Exception): ResponseEntity<Response> {
        log.info("request body: $request, error msg: ${e.message}, exception type: ${e::class}")
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                Response(
                    message = "${WithdrawException::class.simpleName}"
                )
            )
    }
}