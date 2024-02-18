package com.ep.api.controller.data

import com.ep.account.data.AccountInfo

data class TransferResponse(
    val withdrawAccountInfo: AccountInfo,
    val depositAccountInfo: AccountInfo
)
