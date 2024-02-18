package com.ep.api.service

import com.ep.account.Command
import com.ep.api.controller.data.AccountResponse
import com.ep.api.controller.data.TransferResponse
import java.math.BigDecimal

interface AccountService : TransferLogic, QueryLogic {
}