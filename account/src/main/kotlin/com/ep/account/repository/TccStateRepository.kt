package com.ep.account.repository

import com.ep.account.domain.TccState
import com.ep.account.domain.TccStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface TccStateRepository : JpaRepository<TccState, Long> {
    fun findByIdAndStatus(tccStateId: Long, status: TccStatus): Optional<TccState>
}