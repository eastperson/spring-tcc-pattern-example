package com.ep.account.data

import com.ep.account.domain.Account
import com.ep.account.domain.TccState

data class TccResult(
    var tccState: TccState,
    var exception: Exception? = null,
    var withdrawAccount: Account? = null,
    var depositAccount: Account? = null
) {

    private fun trySuccess(withdrawTryResult: TryResult) {
        this.tccState.tryLogic()
        this.withdrawAccount = withdrawTryResult.withdrawAccount
    }

    fun confirm(depositAccount: Account?) {
        this.tccState.confirm()
        this.depositAccount = depositAccount
    }

    fun cancel(tryResult: TryResult) {
        this.tccState.cancel()
        this.exception = tryResult.exception
    }

    fun cancel(confirmDepositResult: ConfirmResult) {
        this.tccState.cancel()
        this.exception = confirmDepositResult.exception
    }

    fun cancel(exception: Exception) {
        this.exception = exception
    }

    fun isConfirmSuccess(): Boolean {
        return this.tccState.isConfirm()
    }

    fun isCancelSuccess(): Boolean {
        return this.tccState.isCancel()
    }

    fun applyResult(withdrawTryResult: TryResult) {
        if (!withdrawTryResult.isSuccess()) {
            cancel(withdrawTryResult)
        } else {
            trySuccess(withdrawTryResult)
        }
    }

    fun applyResult(confirmDepositResult: ConfirmResult) {
        if (!confirmDepositResult.isSuccess()) {
            cancel(confirmDepositResult)
        } else {
            confirm(confirmDepositResult.depositAccount)
        }
    }

    fun applyResult(cancelWithdrawResult: CancelResult, cancelDepositResult: CancelResult) {
        if (!cancelWithdrawResult.isSuccess()) {
            cancel(cancelWithdrawResult.exception!!)
        }
        if (!cancelDepositResult.isSuccess()) {
            cancel(cancelDepositResult.exception!!)
        }
    }

    fun isTrySuccess(): Boolean {
        return this.tccState.isTry()
    }
}

class TryResult(
    var state: State,
    var exception: Exception?,
    var withdrawAccount: Account?
) {
    fun isSuccess(): Boolean {
        return this.state == State.SUCCESS
    }
}

class ConfirmResult(
    var state: State,
    var exception: Exception?,
    var depositAccount: Account?
) {
    fun isSuccess(): Boolean {
        return this.state == State.SUCCESS
    }
}

class CancelResult (
    var state: State,
    var exception: Exception?
) {
    fun isSuccess(): Boolean {
        return this.state == State.SUCCESS
    }
}

enum class State {
    SUCCESS, FAIL
}
