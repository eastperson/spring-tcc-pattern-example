package com.ep.account.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "tcc_state")
class TccState(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var status: TccStatus,
    var created: LocalDateTime
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TccState) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    fun tryLogic() {
        this.status = TccStatus.TRY
    }

    fun confirm() {
        this.status = TccStatus.CONFIRM
    }

    fun cancel() {
        this.status = TccStatus.CANCEL
    }

    fun isTry(): Boolean {
        return this.status == TccStatus.TRY
    }

    fun isConfirm(): Boolean {
        return this.status == TccStatus.CONFIRM
    }

    fun isCancel(): Boolean {
        return this.status == TccStatus.CANCEL
    }
}

enum class TccStatus {
    INIT, TRY, CONFIRM, CANCEL
}