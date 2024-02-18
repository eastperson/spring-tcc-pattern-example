package com.ep.api.service

import com.ep.api.controller.data.AccountResponse

interface QueryLogic {
    fun query(accountNumber: String): AccountResponse?
}