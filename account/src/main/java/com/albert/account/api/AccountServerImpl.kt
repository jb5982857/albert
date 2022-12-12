package com.albert.account.api

import android.content.Context
import com.albert.account.config.RoutePath
import com.albert.application.lifecycle.annotation.AApplicationLifecycle
import com.albert.application.lifecycle.api.IAApplicationLifecycle
import com.albert.common.server.account.AccountCallback
import com.albert.common.server.account.AccountPathApi
import com.albert.common.server.account.IAccountServer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = AccountPathApi.Server, name = "账号服务")
@AApplicationLifecycle
class AccountServerImpl : IAccountServer, IAApplicationLifecycle {
    override fun login(callback: AccountCallback) {
        ARouter.getInstance().build(RoutePath.ACCOUNT_ACTIVITY_PATH).navigation()
    }

    override fun getToken(): String {
        return ""
    }

    override fun logout(callback: AccountCallback) {
    }

    override fun init(context: Context?) {

    }
}