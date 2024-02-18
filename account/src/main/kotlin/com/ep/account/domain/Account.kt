package com.ep.account.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "account")
class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "account_number")
    val accountNumber: String,
    @Column(name = "account_name")
    val accountName: String,
    var balance: BigDecimal
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Account) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    fun deposit(amount: BigDecimal) {
        this.balance += amount
    }

    fun withdraw(amount: BigDecimal): BigDecimal {
        validate(amount)
        this.balance -= amount;
        return amount
    }

    private fun validate(amount: BigDecimal) {
        if (this.balance < amount) {
            throw IllegalStateException()
        }
    }
}