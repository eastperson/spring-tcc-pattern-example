package com.ep.api.service.tcc

import com.ep.account.domain.TccState

interface TccStateService {
    fun save(tccState: TccState)
}