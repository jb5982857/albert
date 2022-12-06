package com.albert.common.server.account

import com.albert.common.server.IBaseServer

object AccountPathApi {
    const val Base = "/account"
    const val Server = "$Base/server"
}

interface IAccountServer : IBaseServer {
    fun login(callback: AccountCallback)

    fun getToken(): String

    fun logout(callback: AccountCallback)
}

interface AccountCallback {
    fun onSuccess()

    fun onFailed()
}