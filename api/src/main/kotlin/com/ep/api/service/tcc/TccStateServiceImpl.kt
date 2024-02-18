package com.ep.api.service.tcc

import com.ep.account.domain.TccState
import com.ep.account.repository.TccStateRepository
import org.springframework.stereotype.Service

@Service
class TccStateServiceImpl(
    val tccStateRepository: TccStateRepository
) : TccStateService {
    override fun save(tccState: TccState) {
        tccStateRepository.save(tccState)
    }
}